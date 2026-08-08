package pe.edu.DAWI_cibertec_demo.controller;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.DAWI_cibertec_demo.model.Producto;
import pe.edu.DAWI_cibertec_demo.repository.ProductoRepository;
import pe.edu.DAWI_cibertec_demo.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Data
public class ProductoController {
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    @PostMapping("/lote")
    public ResponseEntity<String> registrarLote(@RequestBody List<Producto> productos){
        productoService.registrarLote(productos);
        return ResponseEntity.ok("Productos registrados");
    }

    @GetMapping
    public List<Producto> listar(){
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id){
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto nuevo){
        return productoRepository.findById(id)
                .map(prod -> {
                    prod.setNombre(nuevo.getNombre());
                    prod.setPrecio(nuevo.getPrecio());
                    Producto actualizado = productoRepository.save(prod);
                    return ResponseEntity.ok(actualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String texto){
        List<Producto> resultados = productoRepository.buscarPorNombre(texto);
        if(resultados.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscar")
    public Page<Producto> buscar(
            @RequestParam String nombre,
            @RequestParam int page,
            @RequestParam int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }




}
