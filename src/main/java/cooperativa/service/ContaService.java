package cooperativa.service;


import cooperativa.model.*;
import cooperativa.repository.ContaRepository;
import cooperativa.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final CorrentistaService correntistaService;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository,
                        CorrentistaService correntistaService) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.correntistaService = correntistaService;
    }
    private String gerarNumero() {
        long proximo = contaRepository.count() + 1;
        String numero = String.format("%06d", proximo);
        while (contaRepository.existsByNumero(numero)) {
            proximo++;
            numero = String.format("%06d", proximo);
        }
        return numero;
    }

    @Transactional
    public Conta abrir(Long IdCorrentista, TipoDeConta tipoDeConta, BigDecimal limite){
        Correntista correntista = correntistaService.buscarPeloID(IdCorrentista);
        String numero = gerarNumero();
        Conta conta;

        if(tipoDeConta== TipoDeConta.CORRENTE){
            conta = new ContaCorrente(numero, correntista, limite);
        }
        else{
            conta = new ContaPoupanca(numero, correntista);
        }
        return contaRepository.save(conta);
    }

    @Transactional(readOnly = true)
    public List<Conta> listar(){
        return contaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Conta buscarPeloID(long id){
        return contaRepository.findById(id).orElseThrow(()-> new IdNaoEncontradoException("Id "+ id +" da conta não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Conta> listarpeloCorrentista(Long IdCorrentista){
        correntistaService.buscarPeloID(IdCorrentista);
        return contaRepository.findByCorrentistaId(IdCorrentista);
    }

    @Transactional
    public Transacao depositar(Long IdConta, BigDecimal valor){
        Conta conta = buscarPeloID(IdConta);
        conta.depositar(valor);
        contaRepository.save(conta);
        return transacaoRepository.save(new Transacao(TipoDeTransacao.DEPOSITO, valor, conta));
    }

    @Transactional
    public Transacao saque(Long IdConta, BigDecimal valor){
        Conta conta = buscarPeloID(IdConta);
        conta.sacar(valor);
        contaRepository.save(conta);
        return transacaoRepository.save(new Transacao(TipoDeTransacao.SAQUE, valor, conta));
    }

    @Transactional
    public List<Transacao> extrato(Long IdConta){
        buscarPeloID(IdConta);
        return transacaoRepository.findByContaIdOrderByDataDesc(IdConta);
    }

    @Transactional
    public Transacao aplicacaoDoRendimento(Long IdConta, BigDecimal taxa){
        Conta conta = buscarPeloID(IdConta);
        if(conta.getTipo()!=TipoDeConta.POUPANCA){
            throw new RegraDeNegocioException("Rendimento só pode ser para conta poupança");

        }
        BigDecimal rendimento = ((ContaPoupanca) conta).calculaRendimento(taxa);
        contaRepository.save(conta);
        return transacaoRepository.save(new Transacao(TipoDeTransacao.RENDIMENTO, rendimento, conta));
    }

    @Transactional
    public Transacao aplicacaoDoJuros(Long IdConta, BigDecimal taxa){
        Conta conta = buscarPeloID(IdConta);
        if(conta.getTipo()!=TipoDeConta.CORRENTE){
            throw new RegraDeNegocioException("Juros só pode ser cobrado da conta corrente");
        }
        BigDecimal juros = ((ContaCorrente) conta).jurosNegativado(taxa);
        contaRepository.save(conta);
        return transacaoRepository.save(new Transacao(TipoDeTransacao.JUROS, juros, conta));
    }

}
