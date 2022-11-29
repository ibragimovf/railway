package uz.pdp.railway.exception.handler;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.pdp.railway.exception.UserNotFoundException;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundException(UserNotFoundException u) {
        return u.getMessage();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public String internalAuthenticationServiceException(InternalAuthenticationServiceException u) {
        return u.getMessage();
    }
}
