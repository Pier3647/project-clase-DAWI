package pe.edu.DAWI_cibertec_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.DAWI_cibertec_demo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
