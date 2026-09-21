package cooperativa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ContaPoupancaTest {
    private ContaPoupanca conta;

    @BeforeEach
    void criarConta(){
        Correntista correntista = new Correntista("Samuel", "11111111111", "samuel@gmail.com", null);
        conta = new ContaPoupanca("2", correntista);
    }

    @Test
    void sacarTudo(){
        conta.depositar(new BigDecimal("300.00"));
        conta.sacar(new BigDecimal("300.00"));
        assertEquals(new BigDecimal("0.00"), conta.getSaldo());

    }
    @Test
    void saldoInsuficiente(){
        conta.depositar(new BigDecimal("300.00"));

        assertThrows(SaldoInsuficienteException.class, ()-> conta.sacar(new BigDecimal(("500.00"))));
        assertEquals(new BigDecimal("300.00"), conta.getSaldo());

    }

    @Test
    void rendimentoCalculadoSobreOSaldoDaPoupanca(){
        conta.depositar(new BigDecimal("500.00"));
        conta.calculaRendimento(new BigDecimal("0.20"));

        assertEquals(new BigDecimal("600.00"), conta.getSaldo());
    }

    @Test
    void naoPodeRenderZerado(){
        assertThrows(RegraDeNegocioException.class, () -> conta.calculaRendimento(new BigDecimal("0.005")));
    }


}


