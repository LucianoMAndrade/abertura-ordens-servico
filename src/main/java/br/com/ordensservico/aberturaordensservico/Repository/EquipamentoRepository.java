package br.com.ordensservico.aberturaordensservico.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ordensservico.aberturaordensservico.model.Equipamento;
public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {
    
    List<Equipamento> findBySetorId(Integer setorId);    
    
}
