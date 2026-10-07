package br.com.ordensservico.aberturaordensservico.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.ordensservico.aberturaordensservico.model.Equipamento;
import br.com.ordensservico.aberturaordensservico.model.Setor;
import br.com.ordensservico.aberturaordensservico.repository.EquipamentoRepository;
import br.com.ordensservico.aberturaordensservico.repository.SetorRepository;

@Service 
public class EquipamentoService {
    private final EquipamentoRepository equipamentoRepository;
    private final SetorRepository setorRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, SetorRepository setorRepository){
        this.equipamentoRepository = equipamentoRepository;
        this.setorRepository = setorRepository;
    }
    
    public List<Equipamento> listar(){
        return equipamentoRepository.findAll();
    }

    public Optional<Equipamento> buscarPorId(int id){
        return equipamentoRepository.findById(id);
    }

    public List<Equipamento> listarPorSetor(Integer setorId){
        return equipamentoRepository.findBySetorId(setorId);
    }   

    public Optional<Equipamento> cadastrar(Equipamento equipamento){
        Optional<Setor> setorEncontrado = setorRepository.findById(equipamento.getSetor().getId());
        if(setorEncontrado.isEmpty()){
            return Optional.empty();
        }
        Equipamento equipamentoCadastrado = new Equipamento();
        equipamentoCadastrado.setNome(equipamento.getNome());
        equipamentoCadastrado.setNumeroPatrimonio(equipamento.getNumeroPatrimonio());
        equipamentoCadastrado.setSetor(equipamento.getSetor());
        
        return Optional.of(equipamentoRepository.save(equipamentoCadastrado));
    }

    public Optional<Equipamento> alterar(int id, Equipamento equipamentoAtualizado){
        Optional<Equipamento> equipamentoExistente = equipamentoRepository.findById(id);
        if(equipamentoExistente.isEmpty()){
            return Optional.empty();
        }

        Optional<Setor> setor = setorRepository.findById(equipamentoAtualizado.getSetor().getId()); 
        if(setor.isEmpty()){
            return Optional.empty();
        }

        Equipamento equipamento = equipamentoExistente.get();
        equipamento.setNome(equipamentoAtualizado.getNome());
        equipamento.setNumeroPatrimonio(equipamentoAtualizado.getNumeroPatrimonio());
        equipamento.setSetor(setor.get());  

        return Optional.of(equipamentoRepository.save(equipamento));
    }

    public boolean excluir(int id){
        Optional<Equipamento> equipamentoExistente = equipamentoRepository.findById(id);
        if(equipamentoExistente.isEmpty()){
            return false;
        }
        equipamentoRepository.deleteById(id);
        return true;
    }

}
