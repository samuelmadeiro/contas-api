package cooperativa.model;

public class SaldoInsuficienteException extends RegraDeNegocioException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
