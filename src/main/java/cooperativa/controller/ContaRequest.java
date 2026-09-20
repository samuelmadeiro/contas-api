package cooperativa.controller;

import cooperativa.model.TipoDeConta;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ContaRequest {
    @NotNull(message = "É obrigatório ter o id do correntista")
    private Long idCorrentista;

    @NotNull(message = "É obrigatório ter o tipo da conta")
    private TipoDeConta tipoDeConta;

    private BigDecimal limite; //válido apenas pra conta-corrente, pode ser nulo

    public Long getIdCorrentista() {
        return idCorrentista;
    }

    public void setIdCorrentista(Long idCorrentista) {
        this.idCorrentista = idCorrentista;
    }

    public TipoDeConta getTipoDeConta() {
        return tipoDeConta;
    }

    public void setTipoDeConta(TipoDeConta tipoDeConta) {
        this.tipoDeConta = tipoDeConta;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
