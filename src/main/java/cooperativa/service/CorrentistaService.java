package cooperativa.service;

import cooperativa.model.Correntista;
import cooperativa.model.IdNaoEncontradoException;
import cooperativa.model.RegraDeNegocioException;
import cooperativa.repository.CorrentistaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CorrentistaService {

    private final CorrentistaRepository correntistaRepository;

    public CorrentistaService(CorrentistaRepository correntistaRepository) {
        this.correntistaRepository = correntistaRepository;
    }

    @Transactional
    public Correntista cadastrar(Correntista correntista){
        if(correntistaRepository.existsBydocumentoCPFouCNPJ(correntista.getDocumentoCPFouCNPJ())){
            throw new RegraDeNegocioException("Já existe um correntista cadastrado com esse documento " + correntista.getDocumentoCPFouCNPJ());
        }
        return correntistaRepository.save(correntista);
    }

    @Transactional(readOnly = true)
    public List<Correntista> listar(){
        return correntistaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Correntista buscarPeloID(Long id){
        return correntistaRepository.findById(id)
                .orElseThrow(() -> new IdNaoEncontradoException("Id "+ id +" do correntista não encontrado"));
    }
}
