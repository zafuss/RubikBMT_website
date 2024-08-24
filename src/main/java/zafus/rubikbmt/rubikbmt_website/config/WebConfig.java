package zafus.rubikbmt.rubikbmt_website.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zafus.rubikbmt.rubikbmt_website.components.BackToSchoolInterceptor;
import zafus.rubikbmt.rubikbmt_website.components.SessionListener;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private BackToSchoolInterceptor backToSchoolInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(backToSchoolInterceptor);
//    }

    @Bean
    public SessionListener sessionListener() {
        return new SessionListener();
    }
}