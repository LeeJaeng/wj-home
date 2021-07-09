package org.woojeong.config;

import lombok.RequiredArgsConstructor;
import org.woojeong.util.QueryStringArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final QueryStringArgumentResolver argumentResolver;

    @Value("${data-root}")
    private String dataRoot;
    @Value("${data-url-root}")
    private String dataUrlRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(dataUrlRoot + "/**")
                .addResourceLocations("file://" + dataRoot);
    }


    @Override
    public void addArgumentResolvers(
            final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(argumentResolver);
    }
}
