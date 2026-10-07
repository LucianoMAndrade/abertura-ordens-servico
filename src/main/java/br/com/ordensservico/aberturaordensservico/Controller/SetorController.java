package br.com.ordensservico.aberturaordensservico.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ordensservico.aberturaordensservico.Model.Setor;
import br.com.ordensservico.aberturaordensservico.Service.SetorService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/setor")
public class SetorController {
    private final SetorService setorService;

    public SetorController(SetorService setorService){
        this.setorService = setorService;
    }

    @GetMapping
    public ResponseEntity<List<Setor>> listarTodos(){
        List<Setor> setores = setorService.listarTodos();
        return ResponseEntity.ok(setores);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Setor> buscarPorId(@PathVariable int id){
        Optional<Setor> setor = setorService.buscarPorId(id);
        if (setor.isPresent()){
            return ResponseEntity.ok(setor.get());
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping 
    public ResponseEntity<Setor> cadastrar(@Valid @RequestBody Setor setor){
        Setor novoSetor = setorService.cadastrar(setor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSetor);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Setor> atualizar(@PathVariable int id, @Valid @RequestBody Setor setorAtualizado){
        Optional<Setor> setor = setorService.atualizar(id, setorAtualizado);
       
        if (setor.isPresent()){
            return ResponseEntity.ok(setor.get());
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id){
        boolean excluido = setorService.excluir(id);
        if (excluido){
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.notFound().build();
        }
    }
}
