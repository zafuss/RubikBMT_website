package zafus.rubikbmt.rubikbmt_website.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import zafus.rubikbmt.rubikbmt_website.entities.User;
import zafus.rubikbmt.rubikbmt_website.services.UserService;

import java.io.IOException;

@Configuration
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String gRecaptchaReponse = request.getParameter("g-recaptcha-response");
        User user = ((User)authentication.getPrincipal());
        request.getSession().setAttribute("userLogin", user);
//        if(!user.isStatus()){
//            userService.resetLockAccount(user);
//        }else{
//            String errorMessage;
//            if (user.getLockExpired() == null) {
//                errorMessage = "Vui lòng liên hệ Admin";
//                request.getSession().setAttribute("errorMessage", errorMessage);
//                setDefaultTargetUrl("/login?error=true");
//                super.onAuthenticationSuccess(request, response, authentication);
//            } else {
//                if(user.getLockExpired().getTime() > System.currentTimeMillis()){
//                    errorMessage = "Tài Khoản Bị Khóa";
//                    request.getSession().setAttribute("errorMessage", errorMessage);
//                    setDefaultTargetUrl("/login?error=true");
//                    super.onAuthenticationSuccess(request, response, authentication);
//                }
//            }
//            userService.resetLockAccount(user);
//        }

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("SuperAdmin"));
        boolean isModifier = authentication.getAuthorities().contains(new SimpleGrantedAuthority("Modifier"));
        if(isAdmin || isModifier){
            setDefaultTargetUrl("/Admin");
        } else {
            setDefaultTargetUrl("/");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
