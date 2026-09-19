package cooperativa.controller;

import cooperativa.model.Correntista;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CorrentistaRequest {
    @NotBlank(message = "É obrigatório ter nome")
    private String nome;

    @NotBlank(message = "É obrigatório ter documento")
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "O documento deve ter ou 11(cpf) dígitos ou 14(CNPJ), recebendo apenas números")
    private String documento;

    @NotBlank(message = "É obrigatório ter email")
    @Email(message = "Esse email é inválido")
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone inválido, deverá conter 10 ou 11 dígitos")
    private String telefone;

    public Correntista toCorrentista(){
        return new Correntista(nome, documento, email, telefone);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
