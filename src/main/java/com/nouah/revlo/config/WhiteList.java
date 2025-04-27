package com.nouah.revlo.config;

import lombok.Data;

import static com.nouah.revlo.constants.UrlConstant.*;

@Data
public class WhiteList {

    public static final String[] NO_AUTH_ROUTES = {
            SIGNUP,LOGIN,"/v3/api-docs",
            "/v3/api-docs/**", "/swagger-ui.html","/swagger-ui/**"
    };


    public static final String[] CSRF_ROUTES = {
           SIGNUP,LOGIN,CLIENT_ONBOARDING,

    };

    public static final String[] ADMIN_ROUTES = {
            "/api/clients/",
            "/api/clients/{clientId}","/api/sales/","/api/products","/api/products/view-all"
    };

}
