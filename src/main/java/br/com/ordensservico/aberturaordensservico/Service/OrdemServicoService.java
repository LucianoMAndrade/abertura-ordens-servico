package br.com.ordensservico.aberturaordensservico.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ordensservico.aberturaordensservico.dto.EncerramentoOrdemServicoRequest;
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

    public List<OrdemServico> listarAbertas() {
        return ordemServicoRepository.findByDataEncerramentoIsNull();
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

    public Optional<OrdemServico> encerrar(Integer id, EncerramentoOrdemServicoRequest dados){
        Optional<OrdemServico> encontrada = ordemServicoRepository.findById(id);
        
        if (encontrada.isEmpty()){
            return Optional.empty();
        }

        OrdemServico ordemServico=encontrada.get();
        
        if(ordemServico.getDataEncerramento()==null){
            ordemServico.setDataEncerramento(LocalDateTime.now());
            ordemServico.setSolucao(dados.getSolucao());
            ordemServicoRepository.save(ordemServico);
            return Optional.of(ordemServico);
        } else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Ordem de Serviço já encerrada.");
        }
    }


}
