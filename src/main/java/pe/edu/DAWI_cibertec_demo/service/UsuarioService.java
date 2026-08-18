package pe.edu.DAWI_cibertec_demo.service;

import pe.edu.DAWI_cibertec_demo.model.Usuario;
import pe.edu.DAWI_cibertec_demo.repository.UsuarioRepository;

import java.util.List;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Usuario obtener(Long id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario login(String correo, String clave){
        Usuario u = usuarioRepository.findByCorreo(correo).orElse(null);

        if(u != null || u.getClave().equals(clave)){
            return  u;
        }
        return null;
    }
}
