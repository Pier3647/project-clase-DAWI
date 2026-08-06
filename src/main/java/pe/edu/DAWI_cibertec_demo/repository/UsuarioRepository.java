package pe.edu.DAWI_cibertec_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.DAWI_cibertec_demo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
