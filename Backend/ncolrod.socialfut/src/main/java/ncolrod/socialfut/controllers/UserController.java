package ncolrod.socialfut.controllers;

import lombok.AllArgsConstructor;
import ncolrod.socialfut.entities.Role;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.UserRepository;
import ncolrod.socialfut.requests.PlayerStatsUpdateRequest;
import ncolrod.socialfut.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Endpoint para obtener los detalles del usuario autenticado.
     *
     * @param userDetails los detalles del usuario autenticado
     * @return una respuesta con los detalles del usuario o un error si no se encuentra
     */
    @GetMapping(value = "getuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para actualizar la posición del usuario autenticado.
     *
     * @param userDetails los detalles del usuario autenticado
     * @param position la nueva posición del usuario
     * @return una respuesta con los detalles actualizados del usuario o un error si no se encuentra
     */
    @PutMapping("/position")
    public ResponseEntity<User> updatePosition(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String position) {
        User user = (User) userDetails;
        if (user != null) {
            user.setPosition(position);
            userRepository.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint para actualizar el perfil del usuario autenticado.
     *
     * @param userDetails los detalles del usuario autenticado
     * @param updatedUser los nuevos datos del usuario
     * @return una respuesta con los detalles actualizados del usuario o un error si ocurre un problema
     */
    @PutMapping("/updateProfile")
    public ResponseEntity<User> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User updatedUser) {
        User user = (User) userDetails;
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            User savedUser = userService.updateUserProfile(user.getId(), updatedUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para eliminar el perfil del usuario autenticado.
     *
     * @param userDetails los detalles del usuario autenticado
     * @param password la contraseña del usuario
     * @return una respuesta indicando si el perfil fue eliminado exitosamente o si hubo un error
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String password) {
        User user = (User) userDetails;
        if (user != null) {
            boolean success = userService.deleteUserProfile(user, password);
            if (success) {
                return ResponseEntity.ok("Perfil eliminado exitosamente.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña incorrecta.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Endpoint para actualizar las estadísticas de los jugadores.
     *
     * @param playerStats una lista de objetos PlayerStatsUpdateRequest con las estadísticas a actualizar
     * @return una respuesta indicando si la actualización fue exitosa o si hubo un error
     */
    @PostMapping("/updateStats")
    public ResponseEntity<Void> updatePlayerStats(@RequestBody List<PlayerStatsUpdateRequest> playerStats) {
        try {
            userService.updatePlayerStats(playerStats);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint para actualizar el rol de un usuario.
     *
     * @param userDetails los detalles del usuario autenticado
     * @param userId el ID del usuario cuyo rol se va a actualizar
     * @param role el nuevo rol a asignar
     * @return una respuesta indicando si el rol fue actualizado exitosamente o si hubo un error
     */
    @PutMapping("/updateRole")
    public ResponseEntity<String> updateRole(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int userId,
            @RequestParam String role) {
        User admin = (User) userDetails;
        if (admin.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para cambiar roles.");
        }
        try {
            Role newRole = Role.valueOf(role.toUpperCase());
            boolean success = userService.updateRole(userId, newRole, admin);
            if (success) {
                return ResponseEntity.ok("El rol del usuario ha sido actualizado exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Error al actualizar el rol del usuario.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Rol inválido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor.");
        }
    }
}
