package com.bargetor.nest.common.http

import org.apache.http.client.config.RequestConfig

class HttpRequestConfig{
    lateinit var requestConfig: RequestConfig
    var retry: Int = 3
}
