package ncolrod.socialfut.auth;

import lombok.RequiredArgsConstructor;
import ncolrod.socialfut.requests.RegisterRequest;
import ncolrod.socialfut.services.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación y registro de usuarios.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request la solicitud de registro con la información del usuario
     * @return una respuesta de autenticación con el token JWT si el registro es exitoso
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para autenticar un usuario.
     *
     * @param request la solicitud de autenticación con el email y la contraseña del usuario
     * @return una respuesta de autenticación con el token JWT si la autenticación es exitosa
     */
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
