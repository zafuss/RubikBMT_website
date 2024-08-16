package zafus.rubikbmt.rubikbmt_website.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class BackToSchoolInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String serverName = request.getServerName();
        if (serverName.startsWith("backtoschool")) {
            request.setAttribute("isBackToSchool", true);
        } else {
            request.setAttribute("isBackToSchool", false);
        }
        return true;
    }
}