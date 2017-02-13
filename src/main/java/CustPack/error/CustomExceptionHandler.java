package CustPack.error;

/**
 * Created by Gal on 08/02/2017.
 */

import CustPack.util.LoggerWritter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

//  Global Error handler, for catching relevant errors,
// validation(annotations)and spring data- such as TypeMismatchException, MissingServletRequestParameterException,
//                                          MethodArgumentTypeMismatchException,ConstraintViolationException
// The errors don't include what jackson handle (the json [parser in spring).
// Main reason for this class is to centralize the error handle and logging,
// and for display limited error description (without the technology description - spring framework, mongodb)

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // 400 - doesn't fit the entity validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LoggerWritter.WriteError(this.getClass(), request.toString(), ex.toString());

        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }


    // 405 - like patch
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LoggerWritter.WriteError(this.getClass(), request.toString(), ex.toString());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));


        return new ResponseEntity<Object>(builder.toString(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 415 - happen when rest client send media that not supported such as XML, text
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        LoggerWritter.WriteError(this.getClass(), request.toString(), ex.toString());
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

        return new ResponseEntity<Object>( builder.substring(0, builder.length() - 1), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // 500 - happen when try to create customer with the same objectid
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        LoggerWritter.WriteError(this.getClass(), request.toString(), ex.toString());
        return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR); //not let user know what happen under the hood
    }
}