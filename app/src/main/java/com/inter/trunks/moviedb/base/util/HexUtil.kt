package com.inter.trunks.moviedb.base.util

import java.io.ByteArrayOutputStream
import java.util.*

private val HEX_CHARS = "0123456789ABCDEF"
private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()

fun String.hexStringToByteArray(): ByteArray {

    val result = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        val firstIndex = HEX_CHARS.indexOf(this[i])
        val secondIndex = HEX_CHARS.indexOf(this[i + 1])

        val octet = firstIndex.shl(4).or(secondIndex)
        result.set(i.shr(1), octet.toByte())
    }

    return result
}

fun ByteArray.toHex(): String {
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS_ARRAY[firstIndex])
        result.append(HEX_CHARS_ARRAY[secondIndex])
    }

    return result.toString()
}

/**
 * Utility method to concatenate two byte arrays.
 * @param first First array
 * @param rest Any remaining arrays
 * @return Concatenated copy of input arrays
 */
fun concatArrays(first: ByteArray, rest: List<ByteArray>): ByteArray {
    var totalLength = first.size
    for (array in rest) {
        totalLength += array.size
    }

    val result = Arrays.copyOf(first, totalLength)
    var offset = first.size
    for (array in rest) {
        System.arraycopy(array, 0, result, offset, array.size)
        offset += array.size
    }
    return result
}

fun append(arrays: Array<ByteArray>): ByteArray {
    val out = ByteArrayOutputStream()
    if (arrays != null) {
        for (array in arrays) {
            if (array != null) {
                out.write(array, 0, array.size)
            }
        }
    }
    return out.toByteArray()
}


fun ByteArray.getUIntAt(idx: Int) =
    ((this[idx].toUInt() and 0xFFu) shl 24) or
            ((this[idx + 1].toUInt() and 0xFFu) shl 16) or
            ((this[idx + 2].toUInt() and 0xFFu) shl 8) or
            (this[idx + 3].toUInt() and 0xFFu)
