package com.btyhub.lib.common.configuration;

import com.btyhub.lib.common.filter.RequestResponseLoggingFilter;
import com.btyhub.lib.common.filter.TransactionIdFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.btyhub.lib.common")
@PropertySource({"classpath:default-application.properties"})
public class CommonConfig {

    @Bean
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter();
    }

    @Bean
    public TransactionIdFilter transactionIdFilter() { return new TransactionIdFilter(); }
}
