package br.com.ordensservico.aberturaordensservico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipamentoRequest {
    public EquipamentoRequest(){
    }
    
    @NotNull (message="O ID do setor é obrigatório.")
    private Integer setorId;

    @NotBlank(message = "O nome do equipamento é obrigatório.")
    private String nome;
    
    @NotBlank(message = "O número do patrimonio é obrigatório.")
    private String numeroPatrimonio;
 
    public Integer getSetorId(){
        return setorId;
    }

    public void setSetorId(Integer setorId ){
        this.setorId=setorId;
    }

    public String getNome() {
        return nome;
    }   

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroPatrimonio() {
        return numeroPatrimonio;
    }

    public void setNumeroPatrimonio(String numeroPatrimonio) {
        this.numeroPatrimonio = numeroPatrimonio;
    }

}
