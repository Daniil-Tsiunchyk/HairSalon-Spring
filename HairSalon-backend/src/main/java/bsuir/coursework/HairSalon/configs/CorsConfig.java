package bsuir.coursework.HairSalon.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // Разрешить доступ с любых источников (в будущем можно заменить на доменное имя)
        corsConfiguration.addAllowedMethod("*"); // Разрешить все HTTP-методы (GET, POST, PUT, DELETE, и др.)
        corsConfiguration.addAllowedHeader("*"); // Разрешить все заголовки

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Применить настройки ко всем URL-ам

        return new CorsFilter(source);
    }
}
