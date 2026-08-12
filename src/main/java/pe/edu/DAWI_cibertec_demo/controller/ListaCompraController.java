package pe.edu.DAWI_cibertec_demo.controller;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.DAWI_cibertec_demo.model.ItemLista;
import pe.edu.DAWI_cibertec_demo.model.ListaCompra;
import pe.edu.DAWI_cibertec_demo.model.Usuario;
import pe.edu.DAWI_cibertec_demo.repository.ItemListaRepository;
import pe.edu.DAWI_cibertec_demo.repository.ListaCompraRepository;
import pe.edu.DAWI_cibertec_demo.repository.UsuarioRepository;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Data
public class ListaCompraController {
    private final ListaCompraRepository listaCompraRepository;
    private final ItemListaRepository itemListaRepository;
    private final UsuarioRepository usuarioRepository;

    //Crear lista de compras
    @PostMapping("/{idUsuario}/crear")
    public ResponseEntity<?> crear(@PathVariable Long idUsuario, @RequestBody ListaCompra listaCompra){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if(usuario == null){
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
        listaCompra.setUsuario(usuario);
        return ResponseEntity.ok(listaCompraRepository.save(listaCompra));
    }

    //Agregar item a lista de compra
    @PostMapping("/{idLista}/agregar-item")
    public ResponseEntity<?> agregarItem(@PathVariable Long idLista, @RequestBody ItemLista itemLista){
        ListaCompra listaCompra = listaCompraRepository.findById(idLista).orElse(null);
        if(listaCompra == null){
            return  ResponseEntity.notFound().build();
        }
        itemLista.setLista(listaCompra);
        return ResponseEntity.ok(itemListaRepository.save(itemLista));
    }

    //Cambiar de estado de item de lista
    @PutMapping("/item/{idItem}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long idItem, @RequestParam String estado){
        return itemListaRepository.findById(idItem)
                .map(item -> {
                    item.setEstado(estado);
                    return ResponseEntity.ok(itemListaRepository.save(item));
                }).orElse(ResponseEntity.notFound().build());
    }

    //Obtener historial de compras
    @GetMapping("/usuario/{idUsuario}")
    public List<ListaCompra> historial(@PathVariable Long idUsuario){
        return listaCompraRepository.findByUsuarioId(idUsuario);
    }

    //Obtener items filtrando por estado
    @GetMapping("/{idLista}/items")
    public ResponseEntity<List<ItemLista>> obtenerItemsPorEstado(
            @PathVariable Long idLista,
            @RequestParam String estado
    ){
        List<ItemLista> items = itemListaRepository.buscarPorEstado(idLista, estado);
        if (items.isEmpty()){
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    //Generar detalles de lista
    @GetMapping("/{idLista}")
    public ResponseEntity<List<ItemLista>> detalle(
            @PathVariable Long idLista
    ){
        List<ItemLista> items = itemListaRepository.detalleLista(idLista);
        if (items.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    //Obtener historial con paginación
    @GetMapping("/usuario/{idUsuario}/paginado")
    public Page<ListaCompra> historialPaginado(
            @PathVariable Long idUsuario,
            @RequestParam int page,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return listaCompraRepository.findByUsuarioId(idUsuario, pageable);
    }

    //Obtener historial con paginacion y ordenamiento dinámico
    @GetMapping("/usuario/{idUsuario}/paginado/ordenado")
    public Page<ListaCompra> historialPaginado(
            @PathVariable Long idUsuario,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "fechaCreacion") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ){
        Sort sort = order.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return listaCompraRepository.findByUsuarioId(idUsuario, pageable);
    }
}
