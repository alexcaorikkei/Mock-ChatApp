package com.example.baseproject.extension

import timber.log.Timber

fun String.toViWithoutAccent(): String {
    var result = ""
    forEach { c ->
        if(c != ' ') {
            result += if("á à ả ã ạ ă ắ ằ ẳ ẵ ặ â ấ ầ ẩ ẫ ậ".contains(c.lowercase())) "a"
            else if("đ".contains(c.lowercase())) "d"
            else if("é è ẻ ẽ ẹ ê ế ề ể ễ ệ".contains(c.lowercase())) "e"
            else if("í ì ỉ ĩ ị".contains(c.lowercase())) "i"
            else if("ó ò ỏ õ ọ ô ố ồ ổ ỗ ộ ơ ớ ờ ở ỡ ợ".contains(c.lowercase())) "o"
            else if("ú ù ủ ũ ụ ư ứ ừ ử ữ ự".contains(c.lowercase())) "u"
            else if("ý ỳ ỷ ỹ ỵ".contains(c.lowercase())) "y"
            else c.lowercase()
        }
    }
    return result
}