package com.example.inghubloan.service.authorization.impl;


import com.example.inghubloan.service.authorization.AuthorizationService;


public class AuthorizationServiceImpl implements AuthorizationService {
    @Override
    public void authorizeAccess(Object resource) {

    }
/*
    public void authorizeAccess(Object resource) {
        Long currentUserId = getCurrentUserId();
        String currentUserRole = getCurrentUserRole();

        if ("ADMIN".equals(currentUserRole)) {
            return;
        }

        Long resourceOwnerId = getResourceOwnerId(resource);

        if (!currentUserId.equals(resourceOwnerId)) {
            throw new SecurityException("Unauthorized access");
        }
    }

    private String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("USER");
        }
        throw new SecurityException("User not authenticated");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getId();
        }
        throw new SecurityException("User not authenticated");
    }

    private Long getResourceOwnerId(Object resource) {
        if (resource instanceof Loan) {
            return ((Loan) resource).getCustomer().getId();
        } else if (resource instanceof Customer) {
            return ((Customer) resource).getId();
        }
        throw new IllegalArgumentException("Unknown resource type");
    }
    *
 */
}