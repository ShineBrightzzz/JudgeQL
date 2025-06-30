package com.hainam.judgeql.shared.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for getting current authenticated user information
 */
@Component
public class SecurityUtils {

    /**
     * Get current authenticated user ID
     * 
     * @return String user ID of the authenticated user
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            // If using UserDetails, the username should be the user ID based on our JWT configuration
            return ((UserDetails) principal).getUsername();
        }
        
        // Fall back to the authentication name
        return authentication.getName();
    }
}
