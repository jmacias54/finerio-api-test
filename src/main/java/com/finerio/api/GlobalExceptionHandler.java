package com.finerio.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.finerio.api.exception.BadRequestException;
import com.finerio.api.exception.InvalidTokenException;
import com.finerio.api.exception.UnknownResourceException;
import com.finerio.api.model.ResponseError;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_BUSINESS_EXCEPTION = "ERROR_BUSINESS_EXCEPTION";
    private static final String BAD_RQT_EXCEPTION = "ERROR_BAD_REQUEST_EXCEPTION";

    
    
    @ExceptionHandler({ InvalidTokenException.class })
    public ResponseEntity<ResponseError> invalidTokenException(Exception ex, HttpServletRequest request) {
        log.error("ServiceException Occured:: URL " + request.getRequestURI() + getParameters(request));
        log.error("Error detail:: ", ex);
        ResponseError responseError = createResponseError(ex, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.toString(),
                "Token invalido, posiblemente expiro.", new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<ResponseError> handleServiceException(Exception ex, HttpServletRequest request) {
        log.error("ServiceException Occured:: URL " + request.getRequestURI() + getParameters(request));
        log.error("Error detail:: ", ex);
        ResponseError responseError = createResponseError(ex, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ERROR_BUSINESS_EXCEPTION, new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ UnknownResourceException.class })
    public ResponseEntity<ResponseError> unknownResourceException(Exception ex, HttpServletRequest request) {
        log.error("ServiceException Occured:: URL " + request.getRequestURI() + getParameters(request));
        log.error("Error detail:: ", ex);
        ResponseError responseError = createResponseError(ex, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString(),
                ERROR_BUSINESS_EXCEPTION, new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ SQLException.class, BadSqlGrammarException.class, DataIntegrityViolationException.class, DataAccessException.class,
            IncorrectResultSizeDataAccessException.class })
    public ResponseEntity<ResponseError> handleSQLException(final Exception ex, final HttpServletRequest request) {
        log.error("SQLException Occured:: URL " + request.getRequestURI() + getParameters(request));
        log.error("Error detail:: ", ex);
        Exception exceptionSQL = new Exception("Error en la base de datos, favor de contactar al administrador de la aplicaci\u00f3n");
        ResponseError responseError = createResponseError(exceptionSQL, HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), ERROR_BUSINESS_EXCEPTION, new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<ResponseError> peticionIncorrecta(final Exception ex, final HttpServletRequest request) {
        log.error("BadRequestException Occured:: URL " + request.getRequestURI() + getParameters(request));
        log.error("Error detail:: ", ex);
        ResponseError responseError = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), BAD_RQT_EXCEPTION,
                new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ RestClientException.class })
    public ResponseEntity<ResponseError> restClientException(Exception ex, WebRequest request) {
        ResponseError responseError = createResponseError(ex, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.toString(),
                "Error en la cominicacion con el servicio , intente ms tarde.", new ArrayList<String>());
        return new ResponseEntity<>(responseError, HttpStatus.SERVICE_UNAVAILABLE);

    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        imprimeError(ex);
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ResponseError body = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), ERROR_BUSINESS_EXCEPTION, errors);

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        imprimeError(ex);
        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ResponseError body = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), ERROR_BUSINESS_EXCEPTION, errors);

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        imprimeError(ex);

        String error = "El valor ingresado => " + ex.getValue() + " es una propiedad de tipo => " + ex.getPropertyName()
                + " ,el tipo correcto a ingresar es => " + ex.getRequiredType();

        ResponseError body = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), ERROR_BUSINESS_EXCEPTION,
                Arrays.asList(error));

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        imprimeError(ex);
        final String error = ex.getRequestPartName() + " parte faltante.";

        ResponseError body = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), ERROR_BUSINESS_EXCEPTION,
                Arrays.asList(error));

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        imprimeError(ex);
        final String error = ex.getParameterName() + " parametro faltante.";

        ResponseError body = createResponseError(ex, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.toString(), ERROR_BUSINESS_EXCEPTION,
                Arrays.asList(error));

        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        imprimeError(ex);
        final String error = "No se ha encontrado ningún controlador para " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ResponseError body = createResponseError(ex, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.toString(), ERROR_BUSINESS_EXCEPTION,
                Arrays.asList(error));

        return handleExceptionInternal(ex, body, headers, HttpStatus.NOT_FOUND, request);
    }

    // 405 - method is not supported for this request. Supported methods are
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        imprimeError(ex);
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append("El método no es compatible con esta solicitud. Los métodos permitidos son");
        for (HttpMethod httpMethod : ex.getSupportedHttpMethods()) {
            builder.append(httpMethod + "");
        }

        HttpRequestMethodNotSupportedException exa = new HttpRequestMethodNotSupportedException(request.getHeader("accept"), String.valueOf(ex
                .getSupportedHttpMethods()));
        ResponseError body = createResponseError(exa, HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.toString(),
                ERROR_BUSINESS_EXCEPTION, Arrays.asList(builder.toString()));

        return handleExceptionInternal(exa, body, headers, HttpStatus.METHOD_NOT_ALLOWED, request);

    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        imprimeError(ex);

        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        for (MediaType mediaType : ex.getSupportedMediaTypes()) {
            builder.append(mediaType + "");
        }

        MediaType mediaType = MediaType.parseMediaType(request.getHeader("accept"));
        HttpMediaTypeNotSupportedException exa = new HttpMediaTypeNotSupportedException(mediaType, ex.getSupportedMediaTypes());
        headers.setAccept(ex.getSupportedMediaTypes());
        ResponseError body = createResponseError(exa, HttpStatus.UNSUPPORTED_MEDIA_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
                ERROR_BUSINESS_EXCEPTION, Arrays.asList(builder.substring(0, builder.length() - 2)));

        return handleExceptionInternal(exa, body, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    private ResponseError createResponseError(Throwable ex, HttpStatus httpStatus, String httpStatusStr, String errorType, List<String> errors) {
        String mensajeRoot = null;

        if (ex != null) {
            if (ex.getMessage() != null) {
                if (ex.getMessage().startsWith("Failed to convert value of type")) {
                    String mensaje = ex.getMessage();
                    mensajeRoot = mensaje.replace("Failed to convert value of type", "Fall\u00f3 al convertir el valor de tipo ")
                            .replace("to required type", "el tipo requerido es").replace("nested exception is", "la exception es");
                } else {
                    mensajeRoot = ex.getMessage() != null ? ex.getMessage() : "";
                }
            }
        }
        return ResponseError.builder().errorCode(httpStatus.value()).rootErrorMessage(mensajeRoot).httpStatus(httpStatusStr).errorList(errors)
                .build();
    }

    private void imprimeError(Throwable ex) {
        log.error("Clase ejecutada  " + ex.getClass().getName());
        log.error("Exception handler executed  " + ex);
    }

    private String getParameters(HttpServletRequest request) {

        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        String ipAddr = getRemoteAddr(request); // : ip;
        if (ipAddr != null && !ipAddr.equals("")) {
            posted.append("&_ip=" + ipAddr);
        }
        String auth = request.getHeader("Authorization");

        if ((auth == null) || !auth.startsWith("Basic ")) {
            final String userAgent = request.getHeader("User-Agent");
            posted.append("&User-Agent=" + userAgent);
        } else {
            posted.append("&Authorization =" + auth);
        }

        if (e != null)
            while (e.hasMoreElements()) {
                Object objectParam = e.nextElement();
                String param = (String) objectParam;
                String value = request.getParameter(param);
                posted.append("&" + param + "=" + value);
            }
        return posted.toString();
    }

    // get the source IP address of the HTTP request
    private String getRemoteAddr(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

}
