package com.csa.app;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ExampleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("{\"message\": \"Hello, World!\"}");
    }
}
