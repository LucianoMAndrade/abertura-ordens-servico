package br.com.ordensservico.aberturaordensservico.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrdemServicoRequest {
    
    @NotNull (message="O ID do Equipamento é obrigatório.")
    private Integer equipamentoId;

    @NotBlank(message = "A descrição da Ordem de Serviço é obrigatória.")
    private String descricao;


    
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

   

    
}
