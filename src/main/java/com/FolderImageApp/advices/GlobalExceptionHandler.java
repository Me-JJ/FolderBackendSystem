package com.FolderImageApp.advices;

import com.FolderImageApp.exception.ResourseNotFound;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(ResourseNotFound.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourseNotFound exc)
    {
        ApiError ae=new ApiError();
        ae.setMessage(exc.getMessage());
        ae.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ApiResponse<>(ae),ae.getStatus());
    }




    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentError(IllegalArgumentException exc)
    {
        ApiError ae=new ApiError();
        ae.setStatus(HttpStatus.BAD_REQUEST);
    ae.setMessage("Invalid Path\n"+exc.getLocalizedMessage());

        return new ResponseEntity<>(new ApiResponse<>(ae),HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exc)
    {
        ApiError ae=new ApiError();
        ae.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        ae.setMessage("Sorry !!, Server not responding. "+exc.getLocalizedMessage());
        return new ResponseEntity<>(new ApiResponse<>(ae),ae.getStatus());

    }
}
