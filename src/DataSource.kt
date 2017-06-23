package dataSource

import org.w3c.dom.Document
import splider.UrlData
import splider.ConfigData
import java.io.File
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

val xmlFilePath = "data.xml"

fun getConfig() : ConfigData {
    val xmldoc = getXmlDoc()
    val mailUser = xmldoc.getElementsByTagName("MailUser").item(0).textContent
    var mailSmtpHost = xmldoc.getElementsByTagName("MailSmtpHost").item(0).textContent
    val mailPassword = xmldoc.getElementsByTagName("MailPassword").item(0).textContent
    var toMail = xmldoc.getElementsByTagName("ToMail").item(0).textContent
    val configData = ConfigData(mailUser,mailPassword,toMail,mailSmtpHost)
    return configData
}

fun addUrl(dataItem: UrlData) {
    val xmldoc = getXmlDoc()
    val items = xmldoc.getElementsByTagName("Items").item(0)
    val item = xmldoc.createElement("Item")
    item.setAttribute("id", dataItem.getElementID())
    item.setAttribute("title", dataItem.title)
    items.appendChild(item)

    val transFactory = TransformerFactory.newInstance()
    val transformer = transFactory.newTransformer()
    transformer.setOutputProperty("encoding", "utf-8")
    transformer.setOutputProperty("indent", "yes")
    val source = DOMSource()
    source.node = xmldoc
    val result = StreamResult()
    result.outputStream = FileOutputStream(xmlFilePath)
    transformer.transform(source, result)
}

fun existsUrl( dataItem: UrlData) : Boolean{
    val xmldoc = getXmlDoc()
    var items = xmldoc.getElementsByTagName("Item")
    var index = 0;
    while (index <= items.length -1 ) {
        var item = items.item(index)
        if( item.attributes.getNamedItem("id").nodeValue == dataItem.getElementID()) {
            return  true
        }
        index++
    }
    return false
}

private fun getXmlDoc() : Document {
    val factory = DocumentBuilderFactory.newInstance()
    factory.isIgnoringElementContentWhitespace = true
    val db = factory.newDocumentBuilder()
    val xmldoc = db.parse(File(xmlFilePath))
    return xmldoc
}