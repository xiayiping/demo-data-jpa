package org.xyp.todoapp.infra

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.servlet.filter.OrderedFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class LogHeaderFilter :  OrderedFilter {
    companion object {
        val log = LoggerFactory.getLogger(LogHeaderFilter::class.java)!!
    }

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {

        log.info("Filter started")
        try {
            chain?.doFilter(request, response)
        } finally {
            log.info("Filter finished")
        }
    }

    override fun getOrder(): Int {
        return Int.MIN_VALUE + 2
    }
}