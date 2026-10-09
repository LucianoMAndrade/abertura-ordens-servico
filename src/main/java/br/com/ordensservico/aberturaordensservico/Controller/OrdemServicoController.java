package br.com.ordensservico.aberturaordensservico.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ordensservico.aberturaordensservico.dto.OrdemServicoRequest;
import br.com.ordensservico.aberturaordensservico.model.OrdemServico;
import br.com.ordensservico.aberturaordensservico.service.OrdemServicoService;
import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/ordem-servicos")
public class OrdemServicoController {
    
    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService){
        this.ordemServicoService=ordemServicoService;

    }

    @GetMapping 
    public List<OrdemServico> listar(){
        return ordemServicoService.listar();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<OrdemServico> listarPorId(@PathVariable Integer id){
        Optional<OrdemServico> ordemServico = ordemServicoService.buscarPorId(id);
        if(ordemServico.isPresent()){
            return ResponseEntity.ok(ordemServico.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping 
    public ResponseEntity<OrdemServico> cadastrar(@Valid @RequestBody OrdemServicoRequest ordemServico){
        System.out.println(ordemServico.getDescricao());
        
        Optional<OrdemServico> novaOrdemServico = ordemServicoService.cadastrar(ordemServico);
        
        if(novaOrdemServico.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(novaOrdemServico.get());
        
    }


}
