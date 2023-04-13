package dev.aknb.ordersystem.controllers;

import dev.aknb.ordersystem.models.ErrorData;
import dev.aknb.ordersystem.models.MessageType;
import dev.aknb.ordersystem.models.Response;
import dev.aknb.ordersystem.models.RestException;
import dev.aknb.ordersystem.services.MessageResolverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    private final MessageResolverService messageResolverService;

    public ExceptionController(MessageResolverService messageResolverService) {
        this.messageResolverService = messageResolverService;
    }

    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<Response<ErrorData>> handleException(RestException exception) {

        String message = messageResolverService.getMessage(exception.getCode(), exception.getArgs());

        if (exception.getFieldName() != null) {

            log.error("{} {} : '{}'", message, exception.getFieldName(), exception.getFieldValue());
            return ResponseEntity
                    .status(exception.getStatus())
                    .body(Response.error(message, exception.getCode()));
        }

        log.error(message);
        return ResponseEntity
                .status(exception.getStatus())
                .body(Response.error(message, exception.getCode()));
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<?> handleFileSizeLimitExceeded(FileSizeLimitExceededException ignoredEx) {

        String message = messageResolverService.getMessage(MessageType.SIZE_LIMIT_EXCEEDED.name());
        log.error(message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.error(message, MessageType.SIZE_LIMIT_EXCEEDED.name()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<FieldError> errors = ex.getFieldErrors();
        List<ErrorData> errorData = errors.stream()
                .map(error ->
                        new ErrorData(messageResolverService.getMessage(error.getDefaultMessage()),
                                error.getDefaultMessage())
                ).collect(Collectors.toList());
        return handleExceptionInternal(ex, errorData, headers, HttpStatus.BAD_REQUEST, request);
    }
}
