package br.com.ordensservico.aberturaordensservico.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.ordensservico.aberturaordensservico.dto.OrdemServicoRequest;
import br.com.ordensservico.aberturaordensservico.model.Equipamento;
import br.com.ordensservico.aberturaordensservico.model.OrdemServico;
import br.com.ordensservico.aberturaordensservico.repository.EquipamentoRepository;
import br.com.ordensservico.aberturaordensservico.repository.OrdemServicoRepository;

@Service 
public class OrdemServicoService {
    private final EquipamentoRepository equipamentoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public OrdemServicoService (EquipamentoRepository equipamentoRepository, OrdemServicoRepository ordemServicoRepository){
        this.equipamentoRepository=equipamentoRepository;
        this.ordemServicoRepository=ordemServicoRepository;
    }

    public List<OrdemServico> listar(){
        return ordemServicoRepository.findAll();
    }

    public Optional<OrdemServico> buscarPorId(int id){
        return ordemServicoRepository.findById(id);
    }

    public Optional<OrdemServico> cadastrar(OrdemServicoRequest ordemServico){

        Optional<Equipamento> equipamentoEncontrado = equipamentoRepository.findById(ordemServico.getEquipamentoId());
        if(equipamentoEncontrado.isEmpty()){
            return Optional.empty();
        }

        OrdemServico ordemServicoCadastrado = new OrdemServico();

        ordemServicoCadastrado.setDescricao(ordemServico.getDescricao());
        ordemServicoCadastrado.setDataAbertura(LocalDateTime.now());
        ordemServicoCadastrado.setEquipamento(equipamentoEncontrado.get());
        
        return Optional.of(ordemServicoRepository.save(ordemServicoCadastrado));
    }


}
