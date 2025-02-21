package ru.netology.cloudservice.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Configuration
public class LoggingFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilterConfig.class);

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("Received request: ");

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> responseLoggingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter((request, response, chain) -> {

            if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
                chain.doFilter(wrappedRequest, wrappedResponse);

                String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
                logger.info("Request [{} {}] Body: {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), requestBody);

                String responseBody = new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
                logger.info("Response [{}] Status: {} Body: {}", wrappedRequest.getRequestURI(), wrappedResponse.getStatus(), responseBody);
                wrappedResponse.copyBodyToResponse();

            } else {
                chain.doFilter(request, response);
            }
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}