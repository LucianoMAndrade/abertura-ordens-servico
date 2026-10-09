package br.com.ordensservico.aberturaordensservico.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrdemServicoRequest {
    
    @NotNull (message="O ID do Equipamento é obrigatório.")
    private Integer equipamentoId;

    @NotBlank(message = "A descrição da Ordem de Serviço é obrigatória.")
    private String descricao;

    private LocalDateTime dataAbertura;

    
    public OrdemServicoRequest(){

    }

    public void setEquipamentoId(Integer equipamentoId){
        this.equipamentoId=equipamentoId;
    }
    public Integer getEquipamentoId() {
        return equipamentoId;
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

    
}
