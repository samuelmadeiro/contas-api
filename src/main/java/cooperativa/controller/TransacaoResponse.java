package cooperativa.controller;

import cooperativa.model.TipoDeTransacao;
import cooperativa.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoResponse {
    private Long id;
    private TipoDeTransacao tipoDeTransacao;
    private BigDecimal valor;
    private LocalDateTime data;

    public TransacaoResponse(Transacao transacao) {
        this.id = transacao.getId();
        this.tipoDeTransacao = transacao.getTipo();
        this.valor = transacao.getValor();
        this.data = transacao.getData();
    }

    public Long getId() {
        return id;
    }

    public TipoDeTransacao getTipoDeTransacao() {
        return tipoDeTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }
}
