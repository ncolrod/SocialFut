package ncolrod.socialfut.services;

import lombok.RequiredArgsConstructor;
import ncolrod.socialfut.auth.AuthenticationRequest;
import ncolrod.socialfut.auth.AuthenticationResponse;
import ncolrod.socialfut.requests.RegisterRequest;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar la autenticación y el registro de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request la solicitud de registro con la información del usuario
     * @return una respuesta de autenticación con el token JWT
     */
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .telephone(request.getTelephone())
                .location(request.getLocation())
                .position(request.getPosition())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Autentica un usuario con su email y contraseña.
     *
     * @param request la solicitud de autenticación con el email y la contraseña del usuario
     * @return una respuesta de autenticación con el token JWT
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
