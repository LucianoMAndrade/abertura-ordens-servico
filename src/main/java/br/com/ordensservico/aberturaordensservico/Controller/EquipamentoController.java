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

import br.com.ordensservico.aberturaordensservico.Model.Equipamento;
import br.com.ordensservico.aberturaordensservico.Service.EquipamentoService;

@RestController 
@RequestMapping ("/equipamentos")
public class EquipamentoController {
    private final EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService  ){
        this.equipamentoService = equipamentoService;
    }

    @GetMapping 
    public List<Equipamento> listar(){
        return equipamentoService.listar();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Equipamento> listarPorId(@PathVariable Integer id){
        Optional<Equipamento> equipamento = equipamentoService.buscarPorId(id);
        if(equipamento.isPresent()){
            return ResponseEntity.ok(equipamento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping ("/setor/{setorId}")
    public ResponseEntity<List<Equipamento>> listarPorSetor(@PathVariable Integer setorId){
        List<Equipamento> equipamentos = equipamentoService.listarPorSetor(setorId);
        if(equipamentos.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(equipamentos);
        }
        
    }

    @PostMapping 
    public ResponseEntity<Equipamento> cadastrar(@RequestBody Equipamento equipamento){
        Optional<Equipamento> novoequipamento = equipamentoService.cadastrar(equipamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoequipamento.get());
        
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Equipamento> atualizar(@PathVariable Integer id, @RequestBody Equipamento equipamentoAtualizado){
        
        Optional<Equipamento> equipamento = equipamentoService.alterar(id, equipamentoAtualizado);
        if(equipamento.isPresent()){
            return ResponseEntity.ok(equipamento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){
        boolean excluido = equipamentoService.excluir(id);
        if(excluido){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
