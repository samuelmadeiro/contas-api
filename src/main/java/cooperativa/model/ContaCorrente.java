package cooperativa.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends Conta {
    
    @Override
    public TipoDeConta getTipo() {
        return TipoDeConta.CORRENTE;
    }

    @Column(precision = 19, scale = 2)
    private BigDecimal limite = BigDecimal.ZERO;

    public ContaCorrente() {
    }

    public ContaCorrente(String numero, Correntista correntista, BigDecimal limite) {
        super(numero, correntista);
        this.limite = limite == null ? BigDecimal.ZERO : limite;
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarTransacao(valor);
        BigDecimal disponivel = getSaldo().add(limite);
        if (valor.compareTo(disponivel) > 0) {
            throw new SaldoInsuficienteException(
                    "Sem saldo suficiente. Valor disponível: " + disponivel);
        }
        setSaldo(getSaldo().subtract(valor));
    }

    public BigDecimal jurosNegativado(BigDecimal taxa) { //caso a conta esteja negativada há juros sobre esse valor
        validarTransacao(taxa);

        if (getSaldo().compareTo(BigDecimal.ZERO) >= 0) {
            throw new RegraDeNegocioException("Não há saldo negativo");
        }
        BigDecimal juros = getSaldo().abs().multiply(taxa).setScale(2, RoundingMode.HALF_UP);
        setSaldo(getSaldo().subtract(juros));
        return juros;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
