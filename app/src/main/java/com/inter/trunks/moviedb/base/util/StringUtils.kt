package ru.altarix.registrar.util

const val REGEX_CYRILLIC = "[^а-яёА-ЯЁ.\\-\\s]"


fun String.clearCyrillic():String {
   return this.replace(REGEX_CYRILLIC.toRegex(), "")
}
fun substringTextFirst(name: String): String {
    val chars = name.toCharArray()
    chars.forEachIndexed { index, c ->
        if (!c.toString().matches("[а-яёА-ЯЁ]".toRegex()))
            return name.substring(0 until index)
    }
    return name
}

fun substringTextLast(name: String): String {
    val chars = name.toCharArray()
    for (i in chars.size downTo 1) {
        if (chars[i - 1].toString().matches("[а-яёА-ЯЁ]".toRegex())) {
            return name.substring(0 until i)
        }
    }
    return name
}

fun firstCharInRegex(name: String): Boolean {
    return name.first().toString().matches("[А-ЯЁ]".toRegex())
}
fun lastCharInRegex(name: String): Boolean {
    return name.last().toString().matches("[а-яё]".toRegex())
}


fun checkUppercaseCount(name: String): Boolean {
    val chars = name.toCharArray()
    var countUpp = 0
    for (c in chars) {
        if (c.isUpperCase()) {
            countUpp++
            if (countUpp > 1) return true
        }
    }
    return false
}

fun isAlpha(name: String): Boolean {
    val chars = name.toCharArray()
    for (c in chars) {
        if (!Character.isLetter(c)) {
            return false
        }
    }
    return true
}

