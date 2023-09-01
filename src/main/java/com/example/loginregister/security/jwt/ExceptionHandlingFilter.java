package com.example.loginregister.security.jwt;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.loginregister.exception.InvalidJwtTokenException;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;


import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class ExceptionHandlingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            setErrorResponse(response, e);
        } catch (MalformedJwtException e) {
            setErrorResponse(response, e);
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, e);
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, e);
        }
    }
    private void setErrorResponse(HttpServletResponse response, Throwable ex){
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        try {
            String responseJson = convertObjectToJson(errorResponse);
            response.getWriter().write(responseJson);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public String convertObjectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
@AllArgsConstructor
@Data
class ErrorResponse{
    private int status;
    private String message;
}
