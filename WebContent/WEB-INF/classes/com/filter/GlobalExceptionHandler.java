package com.filter;

import com.exception.*;

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

        } catch (InvalidEmailException e) {

            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                e.getMessage()
            );
            throw e; 

        } catch (DuplicateEmailException e) {

            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_CONFLICT,
                e.getMessage()
            );
            throw e;

        } catch (StudentNotFoundException e) {

            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_NOT_FOUND,
                e.getMessage()
            );
            throw e;

        } catch (RuntimeException e) {

            ((HttpServletResponse) response).sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Something went wrong"
            );
            throw e;
        }
    }
}
