package servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.beans.User;
import model.dao.UserDAO;

import java.io.IOException;

/**
 * Guards protected routes and restores sessions via remember-me cookie.
 */
@WebFilter(urlPatterns = {
        "/dashboard",
        "/students",
        "/teachers",
        "/subjects",
        "/classes",
        "/grades",
        "/studentDetails",
        "/search"
})
public class AuthFilter implements Filter {

    private static final String REMEMBER_COOKIE = "rememberedUser";
    private UserDAO userDAO;

    @Override
    public void init(FilterConfig filterConfig) {
        userDAO = new UserDAO();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession(false);
        User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Try to restore the session via remember-me cookie
            String rememberedUsername = getRememberedUsername(req);
            if (rememberedUsername != null) {
                User remembered = userDAO.findByUsername(rememberedUsername);
                if (remembered != null) {
                    HttpSession newSession = req.getSession(true);
                    newSession.setAttribute("currentUser", remembered);
                    // Normalize role to uppercase for consistent comparisons
                    String role = remembered.getRole();
                    newSession.setAttribute("role", role != null ? role.toUpperCase() : null);
                    currentUser = remembered;
                    session = newSession;
                } else {
                    clearCookie(resp, req.getContextPath());
                }
            }
            if (currentUser == null) {
                resp.sendRedirect(req.getContextPath() + "/auth");
                return;
            }
        }

        if (isParentAccessingRestrictedPath(req, session)) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?denied=1");
            return;
        }

        chain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}

    private String getRememberedUsername(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (REMEMBER_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void clearCookie(HttpServletResponse resp, String contextPath) {
        Cookie cookie = new Cookie(REMEMBER_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath(contextPath == null || contextPath.isBlank() ? "/" : contextPath);
        resp.addCookie(cookie);
    }

    private boolean isParentAccessingRestrictedPath(HttpServletRequest req, HttpSession session) {
        if (session == null) return false;
        String role = (String) session.getAttribute("role");
        if (role == null || !"PARENT".equalsIgnoreCase(role)) {
            return false;
        }
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.startsWith("/students")
                || path.startsWith("/teachers")
                || path.startsWith("/classes")
                || path.startsWith("/grades");
    }
}

