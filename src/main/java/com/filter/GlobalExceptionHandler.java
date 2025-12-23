package com.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalExceptionHandler implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try {
            chain.doFilter(request, response);

        } catch (Exception e) {

            // ✅ response already committed? then DO NOTHING
            if (((HttpServletResponse) response).isCommitted()) {
                return;
            }

            String title = "Application Error";
            String message = "Something went wrong.";

            if (e instanceof NumberFormatException) {
                title = "Invalid Input";
                message = "Please enter valid number.";
            } else if (e instanceof NullPointerException) {
                title = "Missing Data";
                message = "Required data is missing.";
            }

            request.setAttribute("errorTitle", title);
            request.setAttribute("errorMessage", message);

            RequestDispatcher rd =
                    request.getRequestDispatcher("/error.jsp");
            rd.forward(request, response);
        }
    }
}
