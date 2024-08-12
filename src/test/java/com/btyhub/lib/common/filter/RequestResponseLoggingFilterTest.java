package com.btyhub.lib.common.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.btyhub.lib.common.filter.RequestResponseLoggingFilter.BodyCachingRequestWrapper;
import com.btyhub.lib.common.filter.RequestResponseLoggingFilter.CachedBodyServletInputStream;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
@DisplayName("Request and response logging filter tests")
class RequestResponseLoggingFilterTest {

    @Test
    @DisplayName("Log request and response to console")
    void logRequestAndResponse_ok() throws ServletException, IOException {
        RequestResponseLoggingFilter filter = new RequestResponseLoggingFilter();
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        String body = "{\"test\":\"prova\"}";
        servletRequest.addHeader("test", "test");
        servletRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        filter.doFilter(servletRequest, servletResponse, filterChain);

        Assertions.assertInstanceOf(BodyCachingRequestWrapper.class, filterChain.getRequest());
        Assertions.assertInstanceOf(ContentCachingResponseWrapper.class, filterChain.getResponse());
        ServletInputStream inputStream = ((BodyCachingRequestWrapper)filterChain.getRequest()).getInputStream();
        inputStream.setReadListener(null);
        Assertions.assertInstanceOf(CachedBodyServletInputStream.class, inputStream);
        Assertions.assertFalse(inputStream.isFinished());
        Assertions.assertTrue(inputStream.isReady());
        Assertions.assertArrayEquals(body.getBytes(StandardCharsets.UTF_8), inputStream.readAllBytes());
    }

    @Test
    @DisplayName("Log request and response to console but response cached")
    void logRequestAndResponse_butResponseCached_ok() throws ServletException, IOException {
        RequestResponseLoggingFilter filter = new RequestResponseLoggingFilter();
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        String body = "";
        servletRequest.addHeader("test", "test");
        servletRequest.addHeader("prova", "prova");
        servletRequest.setContent(body.getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(servletResponse);

        filter.doFilter(servletRequest, cachedResponse, filterChain);

        Assertions.assertInstanceOf(BodyCachingRequestWrapper.class, filterChain.getRequest());
        Assertions.assertInstanceOf(ContentCachingResponseWrapper.class, filterChain.getResponse());
        ServletInputStream inputStream = ((BodyCachingRequestWrapper)filterChain.getRequest()).getInputStream();
        inputStream.setReadListener(null);
        Assertions.assertInstanceOf(CachedBodyServletInputStream.class, inputStream);
        Assertions.assertTrue(inputStream.isFinished());
        Assertions.assertTrue(inputStream.isReady());
        Assertions.assertArrayEquals(body.getBytes(StandardCharsets.UTF_8), inputStream.readAllBytes());
    }

    @Test
    @DisplayName("Log request and response to console but body is null")
    void logRequestAndResponse_butBodyIsNull_ok() throws ServletException, IOException {
        RequestResponseLoggingFilter filter = new RequestResponseLoggingFilter();
        MockFilterChain filterChain = new MockFilterChain();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader("test", "test");
        servletRequest.setContent(null);
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        filter.doFilter(servletRequest, servletResponse, filterChain);

        Assertions.assertInstanceOf(BodyCachingRequestWrapper.class, filterChain.getRequest());
        Assertions.assertInstanceOf(ContentCachingResponseWrapper.class, filterChain.getResponse());
        ServletInputStream inputStream = ((BodyCachingRequestWrapper)filterChain.getRequest()).getInputStream();
        inputStream.setReadListener(null);
        Assertions.assertInstanceOf(CachedBodyServletInputStream.class, inputStream);
        Assertions.assertTrue(inputStream.isFinished());
        Assertions.assertTrue(inputStream.isReady());
        Assertions.assertNotNull(inputStream.readAllBytes());
    }
}
