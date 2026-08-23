package ar.com.solaresdedonato.api.adapter.in.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.storage.local.base-dir}")
    private String baseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Path.of(baseDir).toAbsolutePath().normalize() + "/";
        registry.addResourceHandler("/media/**").addResourceLocations(location);
    }
}
