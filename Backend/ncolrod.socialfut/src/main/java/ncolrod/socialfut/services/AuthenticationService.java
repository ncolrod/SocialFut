package ncolrod.socialfut.services;

import lombok.RequiredArgsConstructor;
import ncolrod.socialfut.auth.AuthenticationRequest;
import ncolrod.socialfut.auth.AuthenticationRespose;
import ncolrod.socialfut.auth.RegisterRequest;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationRespose register(RegisterRequest request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationRespose.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationRespose authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationRespose.builder()
                .token(jwtToken)
                .build();
    }

}
