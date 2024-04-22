package ncolrod.socialfut.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ncolrod.socialfut.services.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /*
    La extensión de OncePerRequestFilter asegura que la lógica de autenticación basada en JWT se aplique
    de manera efectiva y eficiente a todas las solicitudes HTTP entrantes en la app Spring Boot.
     */

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;


    /*
    Como vemos en este metodo recibimos por paramentros la request que interceptamos, el response para responder a la solicitud y el filtro que usamos para la solicitud
     */

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); //Extraemos el token JWT que lo contiene el Header
        final String jwt;
        final String userEmail;

        /*
        Comprobamos si la solicitud contiene un token JWT valido en el encabezado de autorización.
        Si es así, extrae el token y lo guarda para su posterior procesamiento.
        Si no se proporciona un token o no se proporciona en el formato esperado,
        la solicitud se pasa al siguiente filtro en la cadena sin realizar ninguna acción adicional.
         */
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
        } else {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserEmail(jwt);
            //Comprobamos que el email del usuario y que no se haya registrado ya antes
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }
    }
}
