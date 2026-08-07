package pe.edu.DAWI_cibertec_demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.edu.DAWI_cibertec_demo.model.Producto;
import pe.edu.DAWI_cibertec_demo.repository.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    @PersistenceContext
    private EntityManager em;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    //Insercion por lotes (batch insert)
    @Transactional
    public void registrarLote(List<Producto> productos){
        int i = 0;
        for (Producto producto : productos){
            em.persist(producto);
            i++;
            if(i % 10 == 0){
                em.flush();
                em.clear();
            }
        }
    }

    // Fetching con Entity Graph (solo nombre y precio)
    public List<Producto> listarTodos(){
        return em.createQuery("SELECT p FROM Producto p", Producto.class)
                .setHint("org.hibernate.fetchSize", 5)
                .getResultList();
    }


}
