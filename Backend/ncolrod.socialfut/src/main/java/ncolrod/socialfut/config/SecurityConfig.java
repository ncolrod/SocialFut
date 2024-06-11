package ncolrod.socialfut.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para la aplicación.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http el objeto HttpSecurity
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception en caso de error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF ya que usaremos tokens JWT que son inmunes a CSRF
                .csrf().disable()
                // Configuramos las reglas de autorización de solicitudes
                .authorizeHttpRequests()
                // Permitimos el acceso sin autenticación a las rutas que empiezan con "/auth/**"
                .requestMatchers("/auth/**")
                .permitAll()
                // Cualquier otra solicitud requiere autenticación
                .anyRequest()
                .authenticated()
                .and()
                // Configuramos la gestión de sesiones para que no se guarden en memoria
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Configuramos el proveedor de autenticación
                .authenticationProvider(authenticationProvider)
                // Añadimos nuestro filtro de autenticación JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
