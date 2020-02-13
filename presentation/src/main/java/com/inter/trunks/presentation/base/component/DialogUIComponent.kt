/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.component

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_NEUTRAL
import android.content.DialogInterface.BUTTON_POSITIVE
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isNotEmpty
import com.inter.trunks.presentation.R
import com.inter.trunks.presentation.base.extensions.isSuccess
import com.inter.trunks.presentation.base.extensions.showView
import kotlinx.android.synthetic.main.dialog_message_layout.view.*

/**
 * @author Линг Дам Тхе
 * Dialog component with central entry point
 */
class DialogUIComponent () {

    private val dialogList = mutableListOf<AlertDialog>()

    private val messageList = mutableListOf<DialogMessage>()

    private var counterTV: TextView? = null
    private var countainerFL: FrameLayout? = null

    fun closeDialogs() {
        dialogList.forEach { it.dismiss() }
        clearMessage()
    }

    fun isDialogShowing(): Boolean = dialogList.any { it.isShowing }

    /** Add dialog message to queue */
    fun addDialog(messageDialog: DialogMessage, context: Context) {

        messageList.run {
            if (none { it.code == messageDialog.code && it.message == messageDialog.message }) {
                add(messageDialog)
                subList(if (isDialogShowing()) 1 else 0, size).sort()
                if (isDialogShowing()) notifyDialogAdded() else showDialog(context)
            }
        }
    }

    private fun showDialog(context: Context): DialogUIComponent {
        messageList.run {
            if (isNotEmpty()) {
                buildDialog(context, first())?.let { dialogList.add(it) }
            }
        }
        return this
    }

    private fun proceedDialogCloseAction(dialog: AlertDialog) = messageList.run {
        if (size > 1) {
            remove(first())
            proceedNextDialog()
        } else {
            first().closeAction?.onClick(dialog.getButton(AlertDialog.BUTTON_NEGATIVE))
            clearMessage()
            dialog.dismiss()
        }
    }

    private fun proceedDialogYesAction(dialog: AlertDialog) = messageList.run {
        if (size > 1) {
            first().run {
                yesAction?.onClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE))
                remove(this)
            }
            proceedNextDialog()
        } else {
            first().yesAction?.onClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE))
            clearMessage()
            dialog.dismiss()
        }
    }

    private fun proceedDialogNeutralAction(dialog: AlertDialog) = messageList.run {
        if (size > 1) {
            first().run {
                neutralAction?.onClick(dialog.getButton(AlertDialog.BUTTON_NEUTRAL))
                remove(this)
            }
            proceedNextDialog()
        } else {
            first().neutralAction?.onClick(dialog.getButton(AlertDialog.BUTTON_NEUTRAL))
            clearMessage()
            dialog.dismiss()
        }
    }

    private fun clearMessage() {
        dialogList.clear()
        messageList.clear()
    }

    /**
     * Отображение следующего диалога
     */
    private fun proceedNextDialog() {
        dialogList.lastOrNull { it.isShowing }?.run {
            updateDialogCounter(this)
            val dm = messageList.first()
            rebuildDialog(this, dm)
            findViewById<TextView>(android.R.id.message)?.startAnimation(
                AnimationUtils.loadAnimation(
                    context, R.anim.translate_rigth_to_left
                )
            )
            findViewById<TextView>(androidx.appcompat.R.id.alertTitle)?.startAnimation(
                AnimationUtils.loadAnimation(
                    context, R.anim.translate_rigth_to_left
                )
            )
            setMessage(dm.message)
            setTitle(dm.title)
        }
    }

    /**
     * Перестраивает view диалогового окна
     */
    private fun rebuildDialog(dialog: AlertDialog, dm: DialogMessage) {
        countainerFL?.run {
            if (isNotEmpty()) removeAllViews()
            if (dm.getDialogBuilder() is CustomBuilder) {
                addView((dm.getDialogBuilder() as CustomBuilder).getCustomView())
            }
        }
        setDialogButtons(dialog, dm.getDialogBuilder())
    }

    /**
     * Процесс построения диалогово окна
     * берется определяется ErrorBuilder, OkBuilder, YesOrNoBuilder, CustomBuilder
     */
    private fun buildDialog(context: Context, dm: DialogMessage): AlertDialog? {
        val db = dm.getDialogBuilder()
        val view = getCounterView(context)
        val builder = with(AlertDialog.Builder(context)) {
            setCancelable(db.isCancelable())
            setTitle(dm.title)
            setMessage(dm.message)
            db.isNeedCancelShow().isSuccess { setNegativeButton(db.getCancelText(), null) }
            db.isNeedOkShow().isSuccess { setPositiveButton(db.getOkText(), null) }
            db.isNeedNeutralShow().isSuccess { setNegativeButton(db.getNeutralText(), null) }
            counterTV = view.message_counter
            countainerFL = view.dialog_container
            setView(view)
            this
        }

        try {
            return builder.show().also {
                setDialogButtons(it, db)
            }
        } catch (e: WindowManager.BadTokenException) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("InflateParams")
    private fun getCounterView(context: Context): View =
        LayoutInflater.from(context).inflate(R.layout.dialog_message_layout, null)

    private fun setDialogButtons(dialog: AlertDialog, db: DialogBuilder) = dialog.run {
        setDialogButton(BUTTON_POSITIVE, db.getOkText(), db.isNeedOkShow())
        setDialogButton(BUTTON_NEGATIVE, db.getCancelText(), db.isNeedCancelShow())
        setDialogButton(BUTTON_NEUTRAL, db.getNeutralText(), db.isNeedNeutralShow())
    }

    private fun proceedDialogButtonsAction(dialog: AlertDialog, button: Int) = when (button) {
        BUTTON_POSITIVE -> proceedDialogYesAction(dialog)
        BUTTON_NEGATIVE -> proceedDialogCloseAction(dialog)
        BUTTON_NEUTRAL -> proceedDialogNeutralAction(dialog)
        else -> Unit
    }

    private fun AlertDialog.setDialogButton(button: Int, textButton: Int, isShow: Boolean) {
        with(getButton(button)) {
            showView(isShow)
            setText(textButton)
            setOnClickListener { proceedDialogButtonsAction(this@setDialogButton, button) }
        }
    }

    private fun notifyDialogAdded() = dialogList.lastOrNull { it.isShowing }?.run {
        setDialogCounter(this)
    }

    private fun setDialogCounter(a: AlertDialog) {
        counterTV?.text = "${1}/${messageList.size}"
    }

    private fun updateDialogCounter(a: AlertDialog) = counterTV?.text?.split('/')?.let {
        counterTV?.text = "${it[0].toInt().inc()}/${it[1]}"
    }

    interface DialogBuilder {

        fun isCancelable() = false

        fun isNeedCancelShow(): Boolean

        fun isNeedOkShow(): Boolean

        fun isNeedNeutralShow(): Boolean = false

        fun getOkText(): Int = R.string.all_ok

        fun getCancelText(): Int = R.string.all_cancel

        fun getNeutralText(): Int = R.string.all_continue
    }

    /** Для построения диалога с ошибкой     */
    interface ErrorBuilder : DialogBuilder {
        override fun isNeedOkShow(): Boolean = false
        override fun isNeedCancelShow(): Boolean = true
    }

    /** Для построения диалога с ОК     */
    interface OkBuilder : DialogBuilder {
        override fun isNeedOkShow(): Boolean = true
        override fun isNeedCancelShow(): Boolean = false
    }

    interface YesOrNoBuilder : DialogBuilder {
        override fun isNeedOkShow(): Boolean = true
        override fun isNeedCancelShow(): Boolean = true
        override fun getOkText(): Int = R.string.all_yes
        override fun getCancelText(): Int = R.string.all_no
    }

    interface YesNoNeutralBuilder : DialogBuilder {
        override fun isNeedOkShow(): Boolean = true
        override fun isNeedCancelShow(): Boolean = true
        override fun isNeedNeutralShow(): Boolean = true
        override fun getOkText(): Int = R.string.all_yes
        override fun getCancelText(): Int = R.string.all_no
    }

    /** Для построения кастомного диалога  */
    interface CustomBuilder : DialogBuilder {
        fun getCustomView(): View
    }

    /**
     * Типы диалоговых окон
     */
    enum class DialogType {
        ERROR, YES_OR_NO, OK, CUSTOM, YES_NO_NEUTRAL;
    }

    /**
     * @param code error code for DialogType.Error, for others type 0
     * @param type mandatory
     */
    class DialogMessage(
        val code: Int = 0,
        val title: String? = null,
        var message: String? = null,
        val type: DialogType
    ) : Comparable<DialogMessage> {

        override fun compareTo(other: DialogMessage): Int = other.priority.compareTo(priority)

        var priority: Int = 0
        var closeAction: View.OnClickListener? = null
        var yesAction: View.OnClickListener? = null
        var neutralAction: View.OnClickListener? = null
        var customBuilder: CustomBuilder? = null

        /**
         * If need custom implementation of builder
         */
        var db: DialogBuilder? = null


        /**
         * By type of Dialog getting interface constructor or setted dialog builder
         */
        fun getDialogBuilder(): DialogBuilder = db ?: when (type) {
            DialogType.ERROR -> object : ErrorBuilder {}
            DialogType.YES_OR_NO -> object : YesOrNoBuilder {}
            DialogType.OK -> object : OkBuilder {}
            DialogType.YES_NO_NEUTRAL -> object : YesNoNeutralBuilder {}
            DialogType.CUSTOM -> customBuilder ?: throw RuntimeException("CustomBuilder must be setted ")
        }
    }

    /**
     * Build Dialog message
     */
    interface ProvideDialogMessage {
        fun setMessage(message: String? = null): ProvideDialogMessage
        fun setMessage(@StringRes message: Int): ProvideDialogMessage
        fun setTitle(title: String? = null): ProvideDialogMessage
        fun setTitle(@StringRes title: Int): ProvideDialogMessage
        fun setCode(code: Int = 0): ProvideDialogMessage
        fun setType(type: DialogType): ProvideDialogMessage
        fun build(): DialogMessage
    }

    companion object {
        private const val TAG = "DialogUIComponent"


    }
}

/**
 * Default dialog provider
 */
class DefaultDialogProvider (
    private val application: Application
) : DialogUIComponent.ProvideDialogMessage {
    var callBack: CallBack? = null

    private var _code = 0
    private var _message: String? = null
    private var _title: String? = null
    private var _titleRes: Int? = null
    private var _messageRes: Int? = null
    private lateinit var _type: DialogUIComponent.DialogType

    override fun setMessage(message: String?): DialogUIComponent.ProvideDialogMessage {
        _message = message
        return this
    }

    override fun setMessage(message: Int): DialogUIComponent.ProvideDialogMessage {
        _messageRes = message
        return this
    }

    override fun setTitle(title: String?): DialogUIComponent.ProvideDialogMessage {
        _title = title
        return this
    }

    override fun setTitle(title: Int): DialogUIComponent.ProvideDialogMessage {
        _titleRes = title
        return this
    }

    override fun setCode(code: Int): DialogUIComponent.ProvideDialogMessage {
        _code = code
        return this
    }

    override fun setType(type: DialogUIComponent.DialogType): DialogUIComponent.ProvideDialogMessage {
        _type = type
        return this
    }

    override fun build(): DialogUIComponent.DialogMessage {
        val dm = createDialogMessage()
        callBack?.call(dm)
        return dm
    }

    private fun createDialogMessage(): DialogUIComponent.DialogMessage {
        val dm = DialogUIComponent.DialogMessage(
            _code, _title ?: getStringRes(_titleRes), _message ?: getStringRes(_messageRes), _type
        )
        resetMessageData()
        return dm
    }

    private fun resetMessageData() {
        _code = -1
        _message = null
        _title = null
        _titleRes = null
        _messageRes = null
    }

    private fun getStringRes(resId: Int?): String? =
        if (resId != null) application.getString(resId) else null

    /**
     * Callback for
     * [com.teamdirector.hero.presentation.base.component.ShowDialog]
     */
    interface CallBack {
        fun call(dm: DialogUIComponent.DialogMessage)
    }

    companion object {
        inline fun defaultCallBack(crossinline func: (dm: DialogUIComponent.DialogMessage) -> Unit): CallBack =
            object : CallBack {
                override fun call(dm: DialogUIComponent.DialogMessage) {
                    func(dm)
                }
            }
    }
}
