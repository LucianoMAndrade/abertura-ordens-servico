package br.com.ordensservico.aberturaordensservico.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ordensservico.aberturaordensservico.Model.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {
}
