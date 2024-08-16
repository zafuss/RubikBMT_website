package zafus.rubikbmt.rubikbmt_website.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zafus.rubikbmt.rubikbmt_website.components.BackToSchoolInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private BackToSchoolInterceptor backToSchoolInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(backToSchoolInterceptor);
    }
}