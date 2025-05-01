package com.FolderImageApp.advices;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ApiError
{
    private String message;
    private HttpStatus status;
    private List<String> subErrors;

}
