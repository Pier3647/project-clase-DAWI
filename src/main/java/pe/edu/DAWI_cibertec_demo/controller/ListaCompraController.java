package pe.edu.DAWI_cibertec_demo.controller;

import lombok.Data;
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
}
