package com.amaral.appaluno.domain;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String objectId;
    private String endereco;
    private String fotoUrl;
    private Integer idade;
    private String nome;
    private String telefone;
    private String updatedAt;
    private String createdAt;

    public Student(String nome, String fotoUrl, Integer idade, String telefone, String endereco)
    {
        this.objectId = objectId;
        this.nome = nome;
        this.fotoUrl = fotoUrl;
        this.idade = idade;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}