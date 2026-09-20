package cooperativa.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ValorRequest {
    @NotNull(message = "É obrigatório ter um valor")
    @Positive(message = "É obrigatório ter um valor positivo")
    private BigDecimal valor;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
