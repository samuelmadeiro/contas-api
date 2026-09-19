package cooperativa.controller;

import cooperativa.model.Correntista;
import cooperativa.service.CorrentistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/correntistas")
public class CorrentistaController {
    private final CorrentistaService correntistaService;

    public CorrentistaController(CorrentistaService correntistaService) {
        this.correntistaService = correntistaService;
    }

    @PostMapping
    public ResponseEntity<CorrentistaResponse> cadastrar(@RequestBody @Valid CorrentistaRequest request){
        Correntista cadastrado = correntistaService.cadastrar(request.toCorrentista());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cadastrado.getId())
                .toUri();
        return ResponseEntity.created(location).body(new CorrentistaResponse(cadastrado));
    }

    @GetMapping
    public List<CorrentistaResponse> listar(){
        List<CorrentistaResponse> resposta = new ArrayList<CorrentistaResponse>();
        for (Correntista correntista : correntistaService.listar()){
            resposta.add(new CorrentistaResponse(correntista));
        }
        return resposta;
    }

    @GetMapping("/{id}")
    public CorrentistaResponse buscarPeloId(@PathVariable Long id){
        return new CorrentistaResponse((correntistaService.buscarPeloID(id)));
    }

}
