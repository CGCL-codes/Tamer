package org.swxjava;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CallHandler {

    void handle(HttpServletRequest request, HttpServletResponse response);
}
