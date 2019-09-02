package ru.skillbranch.devintensive.utils

import android.graphics.Color

object ColorHash {

    val S = floatArrayOf(0.35f, 0.5f, 0.65f)
    val L = floatArrayOf(0.35f, 0.5f, 0.65f)


    fun hslFromStr(str: String): FloatArray {
        var hash = BKDRHash(str)

        val H =  (hash % 359).toFloat()
        hash = hash / 360
        val S = this.S[hash % this.S.size]
        hash = (hash / this.S.size)
        val L = this.L[hash % this.L.size]

        return floatArrayOf(H,S,L )
    }

    fun BKDRHash(str: String): Int {
        val seed = 131
        val seed2 = 137
        var hash = 0
        // make hash more sensitive for short string like 'a', 'b', 'c'
        val ourString = str + 'x'
        // Note: Number.MAX_SAFE_INTEGER equals 9007199254740991
        val max_safe_int = 9007199254740991 / seed2
        for( i in 0 until ourString.length) {
            if(hash > max_safe_int) {
                hash = (hash / seed2)
            }
            hash = hash * seed + ourString[i].toInt()
        }
        return hash
    }

    fun getColor(initials: String): Int {
        return Color.HSVToColor(hslFromStr(initials))
    }
}