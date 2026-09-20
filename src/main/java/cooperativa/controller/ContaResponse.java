package cooperativa.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import cooperativa.model.Conta;
import cooperativa.model.ContaCorrente;
import cooperativa.model.TipoDeConta;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)//ignora os campos com valor nulo(limite poupança)
public class ContaResponse {
    private Long id;
    private String numero;
    private TipoDeConta tipoDeConta;
    private BigDecimal saldo;
    private BigDecimal limite;
    private Long idCorrentista;
    private String nomeCorrentista;

    public ContaResponse(Conta conta) {
        this.id = conta.getId();
        this.numero = conta.getNumero();
        this.tipoDeConta = conta.getTipo();
        this.saldo = conta.getSaldo();
        if(conta.getTipo()==TipoDeConta.CORRENTE){
            this.limite = ((ContaCorrente)conta).getLimite();
        }
        this.idCorrentista = conta.getCorrentista().getId();
        this.nomeCorrentista = conta.getCorrentista().getNome();
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public TipoDeConta getTipoDeConta() {
        return tipoDeConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public Long getIdCorrentista() {
        return idCorrentista;
    }

    public String getNomeCorrentista() {
        return nomeCorrentista;
    }
}
