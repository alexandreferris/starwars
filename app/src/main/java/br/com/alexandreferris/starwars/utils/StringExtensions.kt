package br.com.alexandreferris.starwars.utils

import br.com.alexandreferris.starwars.utils.constants.Credentials
import org.apache.commons.lang3.StringUtils

fun String.getUrlId(stringName: String): Int {
    return this.replace(Credentials.BASE_URL, StringUtils.EMPTY)
        .replace("$stringName/", StringUtils.EMPTY)
        .replace("/", StringUtils.EMPTY).toInt()
}

fun String.formatUSDate(): String {
    val splitedDate = this.split("-")
    return splitedDate[2] + "/" + splitedDate[1] + "/" + splitedDate[0]
}

fun String.removeSingleLines(): String {
    return this.replace("\r\n\r\n", "line_spacing")
        .replace("\r\n", " ")
        .replace("line_spacing", "\r\n\r\n")
}