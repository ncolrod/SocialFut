package ncolrod.socialfut.auth;

import lombok.RequiredArgsConstructor;
import ncolrod.socialfut.services.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthenticationRespose> register(
            @RequestBody RegisterRequest request
    ){
        try{
            return ResponseEntity.ok(service.register(request));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AuthenticationRespose> login(
            @RequestBody AuthenticationRequest request
    ){
        try{
            return ResponseEntity.ok(service.authenticate(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }



}
