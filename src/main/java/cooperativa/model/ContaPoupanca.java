package cooperativa.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends Conta {
    @Override
    public TipoDeConta getTipo() {
        return TipoDeConta.POUPANCA;
    }

    public ContaPoupanca() {
    }

    public ContaPoupanca(String numero, Correntista correntista) {
        super(numero, correntista);
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarTransacao(valor);
        if (valor.compareTo(getSaldo()) > 0) {
            throw new SaldoInsuficienteException(
                    "Seu saldo não é o suficiente. Atualmente tens: " + getSaldo()
            );
        }
        setSaldo(getSaldo().subtract(valor));
    }

    public BigDecimal calculaRendimento(BigDecimal taxa) {
        validarTransacao(taxa);
        if (getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("A conta não tem saldo para poder render");
        }
        BigDecimal rendimento = getSaldo().multiply(taxa).setScale(2, RoundingMode.HALF_UP);
        setSaldo(getSaldo().add(rendimento));
        return rendimento;
    }
    
}
