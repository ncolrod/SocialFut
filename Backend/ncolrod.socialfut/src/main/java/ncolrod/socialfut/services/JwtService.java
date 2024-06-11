package ncolrod.socialfut.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Clase servicio para manejar operaciones JWT como generar tokens,
 * extraer reclamaciones y validar tokens.
 */
@Service
public class JwtService {

    // Clave secreta para firmar los tokens JWT
    private static final String SECRET_KEY = "5A7134743777397A24432646294A404E6352666556A686E3272357538782F4125";

    /**
     * Extrae el correo electrónico del usuario del token JWT.
     *
     * @param token el token JWT
     * @return el correo electrónico del usuario
     */
    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim especifico del token JWT.
     *
     * @param <T> el tipo de claim
     * @param token el token JWT
     * @param claimsResolver una función para resolver el claim
     * @return el claim resuelto
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para los detalles del usuario dado.
     *
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims adicionales para los detalles del usuario dado.
     *
     * @param extractClaims las reclamaciones adicionales a incluir en el token
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .header().type("JWT")
                .and()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // No se establece fecha de expiración
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida el token JWT con los detalles del usuario.
     *
     * @param token el token JWT
     * @param userDetails los detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userEmail =  extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername()));
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token el token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     *
     * @param token el token JWT
     * @return la fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todas las reclamaciones del token JWT.
     *
     * @param token el token JWT
     * @return las reclamaciones
     */
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene la clave para firmar el token JWT.
     *
     * @return la clave de firma
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
