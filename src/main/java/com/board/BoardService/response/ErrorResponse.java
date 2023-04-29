package com.board.BoardService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private int status;
    private String message;
    // DTO 멤버 변수 필드의 효성 검증 실패 시 발생하는 에러 정보를 담는 멤버 변수
    private List<FieldError> fieldErrors;

    // URI 변수 값의 유효성 검증 실퍃로 발생하는 에러 정보 담는 멤버 변수
    private List<ConstraintViolationError> violationExceptions;

    // 생성자를 private 으로 지정하여 ErrorResponse 클래스는 new 방식으로 ErrorResponse 객체를 생성할 수 없다.


    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationExceptions) {
        this.fieldErrors = fieldErrors;
        this.violationExceptions = violationExceptions;
    }

    // MethodArgumentNotValidException에 대한 ErrorResponse 객체 생성
    // BindingResult는 에러 정보를 얻는 객체
    /*
    BindingResult 객체를 통해 에러 정보를 추출하고 가공하는 작업은 ErrorResponse 클래스의 static 멤버 클래스 FieldError 클래스에 위임
     */
    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    // ConstraintViolationException에 대한 ErrorResponse 객체 생성
    // Set<ConstraintViolation<?>>는 ConstraintViolationException에서 에러 정보를 얻기 위해 필요한 객체
    /*
    Set<ConstraintViolation<?>> 객체를 통해 에러 정보를 추출하고 가공하는 작업은 ErrorResponse 클래스의 static 멤버 클래스인
    ConstraintViolation 클래스에 위임
     */
    public static ErrorResponse of(Set<ConstraintViolation<?>> violations) {
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus.value(), message);
    }


    // DTO 클래스 멤버 변수의 유효성 검증에서 발생하는 에러 정보 생성
    @Getter
    public static class FieldError{
        private String field;
        private Object rejectedValue;
        private String reason;

        private FieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors =
                    bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }

    // URI 변수 값에 대한 에러 정보 생성
    @Getter
    public static class ConstraintViolationError{
        private String propertyPath;
        private Object rejectedValue;
        private String reason;

        private ConstraintViolationError(String propertyPath, Object rejectedValue, String reason) {
            this.propertyPath = propertyPath;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()))
                    .collect(Collectors.toList());
        }
    }
}
