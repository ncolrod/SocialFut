package ncolrod.socialfut.controllers;

import lombok.AllArgsConstructor;
import ncolrod.socialfut.entities.User;
import ncolrod.socialfut.repositories.UserRepository;
import ncolrod.socialfut.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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



}
