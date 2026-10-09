package br.com.ordensservico.aberturaordensservico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ordensservico.aberturaordensservico.model.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico,Integer>{
    
}
