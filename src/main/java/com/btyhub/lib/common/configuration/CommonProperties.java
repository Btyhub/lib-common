package com.btyhub.lib.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@ConfigurationProperties(prefix = "btyhub.lib.common")
public class CommonProperties {

    private ErrorsProperties errors;

    @Data
    @NoArgsConstructor
    public static class ErrorsProperties {
        private Map<String, ErrorMessage> errorsMessages = Collections.emptyMap();


        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ErrorMessage {
            private String message;
            private String description;
            private String code;
            private int status;
        }
    }
}
