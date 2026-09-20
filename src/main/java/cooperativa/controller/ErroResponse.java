package cooperativa.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponse {

    private LocalDateTime dataHora;
    private int status;
    private String mensagem;
    private List<String> detalhes;

    public ErroResponse(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.dataHora = LocalDateTime.now();
    }

    public ErroResponse( String mensagem, int status, List<String> detalhes) {
        this(mensagem, status);
        this.detalhes = detalhes;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public int getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}
