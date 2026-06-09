package com.example.van_phong_pham.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex) {
        return "redirect:/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, RedirectAttributes redirectAttributes) {
        System.err.println("Global exception caught: " + ex.getMessage());
        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi hệ thống: " + ex.getMessage());
        return "redirect:/home";
    }
}
