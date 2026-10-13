package br.com.ordensservico.aberturaordensservico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ordensservico.aberturaordensservico.model.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico,Integer>{
    List<OrdemServico> findByDataEncerramentoIsNull();
}
