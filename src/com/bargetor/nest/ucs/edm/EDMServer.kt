package com.bargetor.nest.ucs.edm

import io.jstack.sendcloud4j.SendCloud
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.jstack.sendcloud4j.mail.Email



@Component
class EDMServer: InitializingBean{
    @Value("\${sendCloud.apiUser:null}")
    var sendCloudApiUser: String? = null
    @Value("\${sendCloud.apiKey:null}")
    var sendCloudApiKey: String? = null

    internal var sendCloud: SendCloud? = null

    fun send(from: String, fromName: String, to: List<String>, subject: String ,contentHtml: String){
        val email = Email.general()
                .from(from)
                .fromName(fromName)
                .html(contentHtml)          // or .plain()
                .subject(subject)
                .to(to.toTypedArray())
        val result = this.sendCloud?.mail()?.send(email)
    }

    override fun afterPropertiesSet() {
        if (this.sendCloudApiKey != null && this.sendCloudApiUser != null){
            this.sendCloud = SendCloud.createWebApi(this.sendCloudApiUser, this.sendCloudApiKey)
        }
    }
}

fun main(args: Array<String>) {
    val edmServer = EDMServer()
    edmServer.sendCloud = SendCloud.createWebApi("migrant", "k03voG7oi1l2qtRH")
    val email = Email.general()
            .from("test@bargetor.com")
            .fromName("JStack Support")
            .html("<b>Hello World!</b>")          // or .plain()
            .subject("mail title")
            .to("madgin@qq.com")
    val result = edmServer.sendCloud?.mail()?.send(email)
    println(result)
}