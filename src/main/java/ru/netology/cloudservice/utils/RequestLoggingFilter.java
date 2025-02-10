package ru.netology.cloudservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    public RequestLoggingFilter() {
        setIncludeQueryString(true);
        setIncludePayload(true);
        setMaxPayloadLength(10000);
        setIncludeHeaders(true);
        setAfterMessagePrefix("REQUEST DATA: ");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}