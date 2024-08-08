package com.smlj.dailypaper.services;

import jakarta.servlet.http.HttpServletRequest;

public class UrlService {
    public static String GetFullUrl(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return GetFullUrl(request.getRequestURL().toString(), request.getQueryString());
    }

    public static String GetFullUrl(String url, String query) {
        if (query == null) {
            return url;
        }
        return url + "?" + query;
    }
}
