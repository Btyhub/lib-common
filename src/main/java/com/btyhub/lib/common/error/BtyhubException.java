package com.btyhub.lib.common.error;

import com.btyhub.lib.common.model.Error;
import lombok.Getter;

@Getter
public class BtyhubException extends RuntimeException {

    public static final String UNKNOWN_ERROR = "unknown-error";
    public static final String VALIDATION_ERROR = "validation-error";
    public static final String BAD_REQUEST = "bad-request";
    public static final String NOT_FOUND = "not-found";
    public static final String SERVICE_UNAVAILABLE = "service-unavailable";

    private final transient Error error;

    public BtyhubException(Error error) {
        super(error.getKey(), error.getThrowable());
        this.error = error;
    }

    public static BtyhubException of(String key, String... args) {
        final var error = Error
                .builder()
                .key(key)
                .args(args)
                .build();
        return new BtyhubException(error);
    }

}
