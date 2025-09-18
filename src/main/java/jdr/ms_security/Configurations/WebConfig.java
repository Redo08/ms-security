package jdr.ms_security.Configurations;

import jdr.ms_security.Interceptors.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/api/**")  // Va a activarse para todas las que tengan esta ruta
                .excludePathPatterns("/api/public/**"); // Menos esta, como el login.

    }
}