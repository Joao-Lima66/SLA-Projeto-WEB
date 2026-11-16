package com.sistema.sla.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private Integer id;
    private String nome;
    private String matricula;
    private String email;
    private String senha;
    private Perfil perfil;
    private String perfilNome;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public String getPerfilNome() { return perfilNome; }
    public void setPerfilNome(String perfilNome) { this.perfilNome = perfilNome; }
}
