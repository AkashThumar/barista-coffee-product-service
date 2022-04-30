package com.barista.coffee.productservice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.barista.coffee.productservice.bean.ErrorBean;

@ControllerAdvice
public class ProductServiceControllerAdvice extends ResponseEntityExceptionHandler {

	private static final String ERROR_MESSAGE_BCPS_500 = "Internal Server Error";
	private static final String ERROR_CODE_BCPS_500 = "BCPS-500";

	@ExceptionHandler(value = { ProductServiceException.class, Exception.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
		if (exception instanceof ProductServiceException) {
			ProductServiceException serviceException = (ProductServiceException) exception;
			return handleExceptionInternal(serviceException, serviceException.getErrorBean(), new HttpHeaders(),
					serviceException.getStatus(), request);
		} else {
			return handleExceptionInternal(exception, new ErrorBean(ERROR_CODE_BCPS_500, ERROR_MESSAGE_BCPS_500),
					new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}

}
