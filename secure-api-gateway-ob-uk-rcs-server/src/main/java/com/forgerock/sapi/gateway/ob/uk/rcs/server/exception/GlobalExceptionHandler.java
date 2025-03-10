/*
 * Copyright © 2020-2025 ForgeRock AS (obst@forgerock.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.forgerock.sapi.gateway.ob.uk.rcs.server.exception;

import com.forgerock.sapi.gateway.ob.uk.common.error.OBErrorException;
import com.forgerock.sapi.gateway.ob.uk.common.error.OBErrorResponseException;
import com.forgerock.sapi.gateway.ob.uk.common.error.OBRIErrorResponseCategory;
import com.forgerock.sapi.gateway.ob.uk.common.error.OBRIErrorType;
import com.forgerock.sapi.gateway.ob.uk.rcs.api.dto.RedirectionAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.org.openbanking.datamodel.v3.error.OBError1;
import uk.org.openbanking.datamodel.v3.error.OBErrorResponse1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final RcsErrorService rcsErrorService;

    public GlobalExceptionHandler(RcsErrorService rcsErrorService) {
        this.rcsErrorService = rcsErrorService;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<OBError1> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(OBRIErrorType.REQUEST_FIELD_INVALID
                    .toOBError1(error.getDefaultMessage())
                    .path(error.getField())
            );
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(OBRIErrorType.REQUEST_OBJECT_INVALID
                    .toOBError1(error.getDefaultMessage())
                    .path(error.getObjectName())
            );
        }

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.ARGUMENT_INVALID,
                        errors),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_PARAMETER_MISSING
                                .toOBError1(ex.getParameterName()).path(ex.getParameterName())),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        if (ex.getMessage().startsWith("Missing request header")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_HEADER.toOBError1(ex.getMessage())
                    ),
                    request);
        } else if (ex.getMessage().startsWith("Missing cookie")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_COOKIE.toOBError1(ex.getMessage())
                    ),
                    request);
        } else if (ex.getMessage().startsWith("Missing argument")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_ARGUMENT.toOBError1(ex.getMessage())
                    ),
                    request);
        }
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_BINDING_FAILED.toOBError1(ex.getLocalizedMessage())
                ),
                request);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_ARGUMENT_TYPE_MISMATCH
                                .toOBError1(ex.getName(), ex.getRequiredType().getName())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedHttpMethods().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_METHOD_NOT_SUPPORTED.toOBError1(ex.getMethod(), builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatusCode status,
                                                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MEDIA_TYPE_NOT_SUPPORTED
                                .toOBError1(ex.getContentType(), builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.debug("HttpMessageNotReadableException from request: {}", request, ex);
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.valueOf(status.value()),
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MESSAGE_NOT_READABLE
                                .toOBError1((ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatusCode status,
                                                                      WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE.toOBError1(builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.valueOf(status.value()),
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_PATH_VARIABLE_MISSING.toOBError1(ex.getVariableName(), ex.getParameter())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode status,
                                                             WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            OBRIErrorResponseCategory.SERVER_INTERNAL_ERROR, OBRIErrorType.SERVER_ERROR.toOBError1()
                    ),
                    request);
        }
        handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.valueOf(status.value()),
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_UNDEFINED_ERROR_YET.toOBError1(ex.getMessage())
                ),
                request);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {OBErrorResponseException.class})
    protected ResponseEntity<Object> handleOBErrorResponse(OBErrorResponseException ex,
                                                           WebRequest request) {

        return ResponseEntity.status(ex.getStatus()).body(
                new OBErrorResponse1()
                        .code(ex.getCategory().getId())
                        .id(ex.getId() != null ? ex.getId() : request.getHeader("x-fapi-interaction-id"))
                        .message(ex.getCategory().getDescription())
                        .errors(ex.getErrors()));
    }


    @ExceptionHandler(value = {OBErrorException.class})
    protected ResponseEntity<Object> handleOBError(OBErrorException ex,
                                                   WebRequest request) {
        HttpStatus httpStatus = ex.getObriErrorType().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new OBErrorResponse1()
                        .code(httpStatus.name())
                        .id(request.getHeader("x-fapi-interaction-id"))
                        .message(httpStatus.getReasonPhrase())
                        .errors(Collections.singletonList(ex.getOBError())));
    }

    @ExceptionHandler(value = {InvalidConsentException.class})
    protected ResponseEntity<RedirectionAction> handleInvalidConsentException(InvalidConsentException ex) {
        return rcsErrorService.invalidConsentError(ex);
    }

    // Required here because these programming errors can get lost in Spring handlers making debug very difficult
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex,
                                                           WebRequest request) {
        log.error("Internal server error from an IllegalArgumentException", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus).body(
                new OBErrorResponse1()
                        .code(httpStatus.name())
                        .id(request.getHeader("x-fapi-interaction-id"))
                        .message(httpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(value = {HttpMessageConversionException.class})
    protected ResponseEntity<Object> handleHttpMessageConversionException(HttpMessageConversionException ex,
                                                                          WebRequest request) {
        log.debug("An invalid resource format ", ex);
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.INVALID_RESOURCE
                                .toOBError1((ex.getCause() != null) ?
                                        ((ex.getCause().getCause() != null) ? ex.getCause().getCause().getMessage() :
                                                ex.getCause().getMessage())
                                        : ex.getMessage())
                ),
                request);
    }
}
