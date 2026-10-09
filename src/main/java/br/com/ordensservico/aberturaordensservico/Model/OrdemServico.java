package br.com.ordensservico.aberturaordensservico.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "ordem_servico")
public class OrdemServico {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    private LocalDateTime dataAbertura;

    @ManyToOne 
    @JoinColumn (name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    public OrdemServico(){

    }

    public OrdemServico (String descricao, LocalDateTime dataAbertura, Equipamento equipamento){
        this.descricao=descricao;
        this.dataAbertura=dataAbertura;
        this.equipamento=equipamento;
    }

    public Integer getOrdemServicoId() {
        return id;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao=descricao;
    }

    public  LocalDateTime getDataAbertura(){
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura){
        this.dataAbertura=dataAbertura;
    }

    public Equipamento getEquipamento(){
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento){
        this.equipamento=equipamento;
    }

}
