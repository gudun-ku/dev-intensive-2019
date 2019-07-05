package ru.skillbranch.devintensive.extensions

fun String.stripHtml(): String {
    val str = this.replace("\\s+".toRegex(), " ")
    return str.replace("\\<[^>]*>".toRegex(),"")
            .replace("&#\\d+;".toRegex(),"")
            .replace("&amp;","")
            .replace("&quot;","")
            .replace("&lt;","")
            .replace("&gt;","")
            .replace("&amp;","")
}

fun String.truncate(numSymbols: Int = 16): String {
    val str =  this.replace("\\s+".toRegex(), " ").trim()
    return if(str.length <= numSymbols + 1) str else "${str.substring(0,numSymbols + 1)}..."
}


