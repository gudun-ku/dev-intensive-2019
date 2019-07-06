package ru.skillbranch.devintensive.extensions

fun String.stripHtml(): String {

    val str = this.replace("\\<[^>]*>".toRegex(),"")
            .replace("&#\\d+;".toRegex(),"")
            .replace("&amp;","")
            .replace("&quot;","")
            .replace("&lt;","")
            .replace("&gt;","")
            .replace("&amp;","")
            .replace(" +".toRegex(), " ")
    return str
}

fun String.truncate(numSymbols: Int = 16): String {
    val str =  this.replace("\\s+".toRegex(), " ").trim()
    return  if(str.length <= numSymbols)
                str.trim()
            else
                "${str.substring(0,numSymbols).trim()}..."
}


