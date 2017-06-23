package splider

import java.util.*
import java.util.regex.Pattern

class YinWang {
    private fun list()  {
        var listHtml = common.httpGet("http://www.yinwang.org/")
        println(listHtml)
    }

    fun handle() {
        list()
    }
}