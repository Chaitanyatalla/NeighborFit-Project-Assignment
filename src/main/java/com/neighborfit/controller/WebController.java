package com.neighborfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web controller for serving frontend pages
 * 
 * Handles the main application interface and demo functionality.
 */
@Controller
public class WebController {
    
    /**
     * Main application page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
} 