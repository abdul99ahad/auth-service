package org.dev.response;

public class ApiResponseDTO<T> {
    private int code;
    private String message;
    private T data;
}
