package com.sec.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*
    ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/error";
    }

     */
}
