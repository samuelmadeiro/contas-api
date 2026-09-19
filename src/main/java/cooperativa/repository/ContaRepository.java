package cooperativa.repository;

import cooperativa.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByCorrentistaId(Long correntistaId);
    boolean existsByNumero(String numero);
}
