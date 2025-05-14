package com.nouah.revlo.config;

import lombok.Data;

import static com.nouah.revlo.constants.UrlConstant.*;

@Data
public class Routes {

    public static final String[] NO_AUTH_ROUTES = {
            SIGNUP,LOGIN,"/v3/api-docs",
            "/v3/api-docs/**", "/swagger-ui.html","/swagger-ui/**"
    };


    public static final String[] CSRF_ROUTES = {
           SIGNUP,LOGIN,CLIENT_ONBOARDING,CREATE_PRODUCT,CREATE_SALES,SEARCH_PRODUCT,FETCH_PRODUCTS,
            REMOVE_PRODUCT,UPDATE_PRODUCT

    };

    public static final String[] ADMIN_ROUTES = {
            CLIENT_REPORT,CREATE_PRODUCT,CREATE_SALES,CLIENT_ONBOARDING,REMOVE_CLIENT,REMOVE_PRODUCT,
            PRODUCT_REPORT,SALES_REPORT,ALL_PRODUCTS,FETCH_PRODUCTS,FETCH_ALL_CLIENT, UPDATE_PRODUCT,
    };

}
