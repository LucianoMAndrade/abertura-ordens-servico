package br.com.ordensservico.aberturaordensservico.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.ordensservico.aberturaordensservico.Model.Setor;
import br.com.ordensservico.aberturaordensservico.Repository.SetorRepository;

@Service 
public class SetorService {
    private final SetorRepository setorRepository;

    public SetorService(SetorRepository setorRepository){
        this.setorRepository = setorRepository;
    }

    public Setor cadastrar(Setor setor){
        return setorRepository.save(setor);
    }

    public List<Setor> listarTodos(){
        return setorRepository.findAll();
    }

    public Optional<Setor> buscarPorId(int id){
        return setorRepository.findById(id);
    }

    public Optional<Setor> atualizar(int id, Setor setorAtualizado){
        Optional<Setor> setorExistente = setorRepository.findById(id);
        if(setorExistente.isEmpty()){
            return Optional.empty();
        }
        
        Setor setor = setorExistente.get();
        setor.setNome(setorAtualizado.getNome());

        return Optional.of(setorRepository.save(setor));

    }

    public boolean excluir(int id){
        Optional<Setor> setorExistente = setorRepository.findById(id);
        if(setorExistente.isEmpty()){
            return false;
        }
        setorRepository.deleteById(id);
        return true;
    }


}
