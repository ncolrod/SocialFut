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

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping(value = "getuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <User> getUser (@AuthenticationPrincipal UserDetails userDetails){
        try {
            User user = (User) userDetails;
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                // Usuario no encontrado no encontrado
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

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

    @PostMapping("/updateStats")
    public ResponseEntity<Void> updatePlayerStats(@RequestBody List<PlayerStatsUpdateRequest> playerStats) {
        try {
            userService.updatePlayerStats(playerStats);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

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
