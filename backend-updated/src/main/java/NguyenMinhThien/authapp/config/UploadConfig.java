package NguyenMinhThien.authapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves files saved to the local "uploads" directory (e.g. user avatars)
 * over HTTP at /uploads/**. Kept separate from MvcConfig, which serves the
 * bundled Flutter web build from the classpath and must keep matching "/**".
 */
@Configuration
public class UploadConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadDir.replace("\\", "/")
                + (uploadDir.endsWith("/") ? "" : "/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
