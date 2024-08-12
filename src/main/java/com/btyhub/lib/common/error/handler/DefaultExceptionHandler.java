package com.btyhub.lib.common.error.handler;

import com.btyhub.lib.common.configuration.CommonProperties;
import com.btyhub.lib.common.configuration.CommonProperties.ErrorsProperties.ErrorMessage;
import com.btyhub.lib.common.error.BtyhubException;
import com.btyhub.lib.common.mapper.HttpErrorMapper;
import com.btyhub.lib.common.model.Error;
import com.btyhub.lib.common.model.HttpError;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Order
@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class DefaultExceptionHandler {
    private final CommonProperties commonProperties;
    private final HttpErrorMapper httpErrorMapper;

    @ExceptionHandler({BtyhubException.class})
    public ResponseEntity<HttpError> handleException(BtyhubException btyhubException) {

        Error error = btyhubException.getError();
        ErrorMessage errorMessage = retrieveError(error.getKey());
        log.error(errorMessage.getMessage(), btyhubException);

        HttpError httpError = httpErrorMapper.map(errorMessage);
        httpError.setMessage(formatMessage(errorMessage.getMessage(), error.getArgs()));

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class
    })
    public ResponseEntity<HttpError> handleClientException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = retrieveError(BtyhubException.BAD_REQUEST);

        HttpError httpError = httpErrorMapper.map(errorMessage);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<HttpError> handleNoHandlerException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = retrieveError(BtyhubException.NOT_FOUND);
        HttpError httpError = httpErrorMapper.map(errorMessage);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = retrieveError(BtyhubException.VALIDATION_ERROR);

        List<String> fields = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> fields.add(fieldError.getField()));
        HttpError httpError = httpErrorMapper.map(errorMessage);
        httpError.setFields(fields);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpError> handleDefaultException(Exception exception) {
        log.error(exception.getMessage(), exception);
        ErrorMessage errorMessage = retrieveError(BtyhubException.SERVICE_UNAVAILABLE);
        HttpError httpError = httpErrorMapper.map(errorMessage);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    private ErrorMessage retrieveError(@NotNull String key) {
        ErrorMessage errorMessage = commonProperties
                .getErrors()
                .getErrorsMessages()
                .get(key);

        if(Objects.isNull(errorMessage)) return retrieveError(BtyhubException.UNKNOWN_ERROR);
        
        return errorMessage;
    }

    private String formatMessage(String message, Object... args) {
        if(args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }

}
