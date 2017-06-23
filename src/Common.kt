package common

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun httpGet( url : String) : String {
    val httpClient = HttpClients.createDefault()
    val get = HttpGet(url)
    val response = httpClient.execute(get)
    val responseEntity = response.entity
    val statusCode = response.statusLine.statusCode
    val responseText = if(statusCode.toString().startsWith("4") || statusCode.toString().startsWith("5")) {
        statusCode.toString()
    } else {
        EntityUtils.toString(responseEntity)
    }
    return responseText
}

fun sendMail(title: String,content : String) : Boolean{
    val configData = dataSource.getConfig();
    val props = Properties()
    props.put("mail.smtp.host", configData.MailSmtpHost)
    props.put("mail.stmp.user", configData.mailUser)

    //To use TLS
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.password", configData.mailPassword)
    //To use SSL
    props.put("mail.smtp.socketFactory.port", "25")
    props.put("mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.port", "25")


    val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(
                    "397898691@qq.com", configData.mailPassword)// Specify the Username and the PassWord
        }
    })
    val to = configData.toMailAddr
    val from = configData.mailUser
    val subject = title
    val msg = MimeMessage(session)
    try {
        msg.setFrom(InternetAddress(from))
        msg.setRecipient(Message.RecipientType.TO,
                InternetAddress(to))
        msg.subject = subject
        msg.setContent(content,"text/html; charset=utf-8")
        Transport.send(msg)
        println("发送邮件成功")
        return true
    } catch (exc: Exception) {
        println(exc)
        return false
    }
}