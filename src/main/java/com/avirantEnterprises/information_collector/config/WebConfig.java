package com.avirantEnterprises.information_collector.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///D://Intership//information-collector//upload-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/pdfs/**")
                .addResourceLocations("file:///D://Intership//information-collector//pdf-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///D://Intership//information-collector//photo-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/docs/**")
                .addResourceLocations("file:///D://Intership//information-collector//doc-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);

        registry.addResourceHandler("/excels/**")
                .addResourceLocations("file:///D://Intership//information-collector//excel-dir/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
}
