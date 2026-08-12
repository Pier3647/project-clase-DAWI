package pe.edu.DAWI_cibertec_demo.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.DAWI_cibertec_demo.dto.LoginRequest;
import pe.edu.DAWI_cibertec_demo.model.Usuario;
import pe.edu.DAWI_cibertec_demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
@Data
public class AuthController {
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Usuario usuario = usuarioRepository.findByCorreo(loginRequest.getCorreo()).orElse(null);
        if(usuario == null || !usuario.getClave().equals(loginRequest.getClave())){
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        return ResponseEntity.ok(usuario);
    }
}
