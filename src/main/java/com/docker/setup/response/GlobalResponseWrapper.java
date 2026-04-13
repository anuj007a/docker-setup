// src/main/java/com/docker/setup/response/GlobalResponseWrapper.java

package com.docker.setup.response;

import com.docker.setup.exception.GlobalExceptionHandler.ErrorDetails;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    // 1. Decides if this advice should be applied
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // Apply to everything EXCEPT ApiResponse, ErrorDetails, or methods that return void (204)
        return !returnType.getParameterType().equals(ApiResponse.class) &&
                !returnType.getParameterType().equals(Void.TYPE);
    }

    // 2. Performs the wrapping logic
    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        // Skip wrapping if the body is null (e.g., DELETE with no content)
        if (body == null || body instanceof ErrorDetails) {
            return body;
        }

        // --- FIX: Safely retrieve the HTTP status code from the native servlet response ---
        int rawStatus = 200; // Default status

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletResponse httpServletResponse = sra.getResponse();
            if (httpServletResponse != null) {
                rawStatus = httpServletResponse.getStatus();
            }
        }

        // Convert the raw status code into an HttpStatus enum
        HttpStatus status = HttpStatus.valueOf(rawStatus);
        String message = "Request executed successfully.";

        if (status.equals(HttpStatus.CREATED)) {
            message = "Resource created successfully.";
        } else if (status.equals(HttpStatus.NO_CONTENT)) {
            // If the controller returned 204 but had a body (which shouldn't happen),
            // ensure we don't return null data. But usually, the supports() method handles void.
            // If a 204 comes through, we skip wrapping by returning the body (which is null here).
            return body;
        }

        // Return the wrapped response
        return ApiResponse.success(
                body,
                message,
                status
        );
    }
}