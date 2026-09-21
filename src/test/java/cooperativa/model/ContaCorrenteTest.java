package cooperativa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContaCorrenteTest {

    private ContaCorrente conta;

    @BeforeEach
    void criarConta(){
        Correntista correntista = new Correntista("Samuel", "11111111111", "samuel@gmail.com", null);
        conta = new ContaCorrente("2", correntista, new BigDecimal("300.00"));
    }

    @Test
    void aumentarOSaldo(){
        conta.depositar(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void depositoZerado(){
        assertThrows(RegraDeNegocioException.class, () -> conta.depositar(BigDecimal.ZERO));
    }

    @Test
    void deveSacarComLimite() {
        conta.depositar(new BigDecimal("100.00"));
        conta.sacar(new BigDecimal("200.00"));

        assertEquals(new BigDecimal("-100.00"), conta.getSaldo());
    }

    @Test
    void saqueMaiorQueOSaldoMaisLimeite(){
        conta.depositar(new BigDecimal("100.00"));

        assertThrows(SaldoInsuficienteException.class, ()-> conta.sacar(new BigDecimal("401.00")));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());

    }

    @Test
    void jurosSobONegativado(){
        conta.depositar(new BigDecimal("100.00"));
        conta.sacar(new BigDecimal("200.00"));

        BigDecimal juros = conta.jurosNegativado(new BigDecimal("0.03"));


        assertEquals(new BigDecimal("3.00"), juros);
        assertEquals(new BigDecimal("-103.00"), conta.getSaldo());
    }

    @Test
    void jurosComSaldoPositivo(){
        conta.depositar(new BigDecimal("100.00"));
        assertThrows(RegraDeNegocioException.class, () -> conta.jurosNegativado(new BigDecimal("0.03")));
    }
}
