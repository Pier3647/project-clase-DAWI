package pe.edu.DAWI_cibertec_demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.DAWI_cibertec_demo.model.Producto;
import pe.edu.DAWI_cibertec_demo.repository.ProductoRepository;
import pe.edu.DAWI_cibertec_demo.util.FormatoUtil;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final FormatoUtil formatoUtil;

    @PersistenceContext
    private EntityManager em;


    public ProductoService(ProductoRepository productoRepository, FormatoUtil formatoUtil) {
        this.productoRepository = productoRepository;
        this.formatoUtil = formatoUtil;
    }

    //Insercion por lotes (batch insert)
    @Transactional
    public void registrarLote(List<Producto> productos){
        int i = 0;
        for (Producto producto : productos){
            producto.setNombre(formatoUtil.capitalizar(producto.getNombre()));
            em.persist(producto);
            i++;
            if(i % 10 == 0){
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }

    // Fetching con Entity Graph (solo nombre y precio)
    public List<Producto> listarTodos(){
        return em.createQuery("SELECT p FROM Producto p", Producto.class)
                .setHint("org.hibernate.fetchSize", 5)
                .getResultList();
    }

    //Buscar productos + paginado

    public Page<Producto> buscarPorNombre(String texto, Pageable pageable){
        if (texto == null || texto.trim().isEmpty()) {
            return productoRepository.findAll(pageable);
        }
        return productoRepository.findByNombreContainingIgnoreCase(texto.trim(), pageable);
    }


}
