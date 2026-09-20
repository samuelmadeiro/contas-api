package cooperativa.controller;


import cooperativa.model.Conta;
import cooperativa.model.Transacao;
import cooperativa.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> abrir(@RequestBody @Valid ContaRequest request){
        Conta conta = contaService.abrir(request.getIdCorrentista(), request.getTipoDeConta(), request.getLimite());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(conta.getId())
                .toUri();
        return ResponseEntity.created(location).body(new ContaResponse(conta));
    }

    private List<ContaResponse> converter(List<Conta> contas){
        List<ContaResponse> resposta = new ArrayList<ContaResponse>();
        for(Conta conta: contas){
            resposta.add(new ContaResponse(conta));
        }
        return resposta;
    }

    @GetMapping
    public List<ContaResponse> listar(){
        return converter(contaService.listar());
    }

    @GetMapping("/{id}")
    public ContaResponse buscarPeloId(@PathVariable Long id){
        return new ContaResponse(contaService.buscarPeloID(id));
    }

    @PostMapping("/{id}/depositos")
    public ResponseEntity<TransacaoResponse> depositar(@PathVariable Long id, @RequestBody @Valid ValorRequest request){
        Transacao transacao = contaService.depositar(id, request.getValor());

        return ResponseEntity.status(HttpStatus.CREATED).body(new TransacaoResponse(transacao));
    }

    @PostMapping("/{id}/saques")
    public ResponseEntity<TransacaoResponse> sacar(@PathVariable Long id, @RequestBody @Valid ValorRequest request){
        Transacao transacao = contaService.saque(id, request.getValor());

        return ResponseEntity.status(HttpStatus.CREATED).body(new TransacaoResponse(transacao));
    }

    @GetMapping("/{id}/extrato")
    public List<TransacaoResponse> extrato(@PathVariable Long id){
        List<TransacaoResponse> resposta = new ArrayList<TransacaoResponse>();
        for(Transacao transacao: contaService.extrato(id)){
            resposta.add(new TransacaoResponse(transacao));
        }
        return resposta;
    }

    @PostMapping("/{id}/rendimentos")
    public ResponseEntity<TransacaoResponse> aplicarRendimento(@PathVariable Long id, @RequestParam @Valid BigDecimal taxa){
        Transacao transacao = contaService.aplicacaoDoRendimento(id, taxa);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransacaoResponse(transacao));
    }

    @PostMapping("/{id}/juros")
    public ResponseEntity<TransacaoResponse> aplicarJuros(@PathVariable Long id, @RequestParam BigDecimal taxa){
        Transacao transacao = contaService.aplicacaoDoJuros(id, taxa);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TransacaoResponse(transacao));
    }

}

