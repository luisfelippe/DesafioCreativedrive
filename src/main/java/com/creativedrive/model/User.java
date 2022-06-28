package com.creativedrive.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements AbstractEntity 
{
	@Id
    @EqualsAndHashCode.Include
    private String id;

    @NotNull(message = "O campo 'nome' é obrigatório")
    private String nome;

    private String email;

    @NotNull(message = "O campo 'senha' é obrigatório")
    @ToString.Exclude
    private String senha;

    private String endereco;
    private String telefone;

    @NotNull(message = "O campo 'perfil' é obrigatório")
    @Builder.Default
    private String perfil = "USER";

    public User(@NotNull User user)
    {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.email;
        this.senha = user.getSenha();
        this.endereco = user.getEndereco();
        this.telefone = user.getTelefone();
        this.perfil = user.getPerfil();
    }
}
