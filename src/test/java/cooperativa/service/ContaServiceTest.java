package cooperativa.service;

import cooperativa.model.*;
import cooperativa.repository.ContaRepository;
import cooperativa.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CorrentistaService correntistaService;

    @InjectMocks
    private ContaService contaService;


    @Test
    void oDepositoVaiAtualizarOSaldoEVaiRegistrarATransacao() {
        Correntista correntista = new Correntista("Samuel", "11111111111", "samuel@gmail.com", null);
        ContaPoupanca conta = new ContaPoupanca("1", correntista);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(transacaoRepository.save(any(Transacao.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Transacao transacao = contaService.depositar(1L, new BigDecimal("300.00"));

        assertEquals(new BigDecimal("300.00"),conta.getSaldo());
        assertEquals(TipoDeTransacao.DEPOSITO, transacao.getTipo());
        assertEquals(new BigDecimal("300.00"), transacao.getValor());
    }

    @Test
    void vaiFalharQuandoAContaNaoExistir(){
        when(contaRepository.findById(900L)).thenReturn(Optional.empty());
        assertThrows(IdNaoEncontradoException.class, ()-> contaService.buscarPeloID(900L));
    }
}
