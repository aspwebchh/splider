package  splider

import java.util.*
import java.util.regex.Pattern

class Fangshijie {
    private val domain = "http://fangshijie.cn"

    private fun list() :Array<UrlData> {
        val listHtml = common.httpGet("$domain/NewsList/")
        val regex = Pattern.compile("<a[^>]*href=\"([^\"]+)\"[^>]*>\\s*(萧山区.+?签约数据)\\s*<\\/a>")
        val matches = regex.matcher(listHtml)
        val result = ArrayList<UrlData>()
        while(matches.find()) {
            val dataItem = UrlData(url = matches.group(1), title =  matches.group(2))
            result.add(dataItem)
        }
        return Array(result.size, { i-> result[i] })
    }

    private fun detail(data: UrlData): String {
        val url = domain + data.url
        val html = common.httpGet(url)
        val regex = Pattern.compile("<table.+><\\/table>")
        val match = regex.matcher(html)
        if (match.find()) {
            return match.group()
        }
        return ""
    }

    fun handle() {
        val urlDataList = list()
        for(item in urlDataList) {
            if (dataSource.existsUrl(item)) {
                continue
            }
            val content = detail(item)
            val success = common.sendMail(item.title, content)
            if (success) {
                dataSource.addUrl(item)
            }
        }
    }
}