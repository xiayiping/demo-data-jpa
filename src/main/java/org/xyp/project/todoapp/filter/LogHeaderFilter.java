package org.xyp.project.todoapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LogHeaderFilter implements OrderedFilter {

    private static final Logger log = LoggerFactory.getLogger(LogHeaderFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest req
          && servletResponse instanceof HttpServletResponse resp
        ) {
            doFilterInternal(req, resp, filterChain);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    //    @Override
    protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final String method = request.getMethod();
        final String uri = request.getRequestURI();
        log.info("<{} path={} >", method, uri);

        request.getHeaderNames().asIterator().forEachRemaining(iter -> {
            final var hValue = request.getHeader(iter);
            log.info("    Header: {} = {}", iter, hValue);
        });
        final long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (response.getStatus() < 400) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            throw e;
        } finally {
            final var status = response.getStatus();
            log.info("</{} path={} > status [{}] , takes [{} ms]",
              method, uri,
              status, System.currentTimeMillis() - startTime
            );
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
