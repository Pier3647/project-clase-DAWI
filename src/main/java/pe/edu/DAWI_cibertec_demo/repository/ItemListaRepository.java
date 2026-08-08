package pe.edu.DAWI_cibertec_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.DAWI_cibertec_demo.model.ItemLista;

import java.util.List;

public interface ItemListaRepository extends JpaRepository<ItemLista, Long> {
    @Query("SELECT i FROM ItemLista i WHERE i.lista.id = :idLista AND LOWER(i.estado) = LOWER(:estado)")
    List<ItemLista> buscarPorEstado(
            @Param("idLista") Long idLista,
            @Param("estado") String estado
    );

    @Query("SELECT i FROM ItemLista i WHERE i.lista.id = :idLista")
    List<ItemLista> detalleLista(
            @Param("idLista") Long idLista
    );
}
