package cooperativa.repository;

import cooperativa.model.Correntista;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
    boolean existsBydocumentoCPFouCNPJ(String documento);
}
