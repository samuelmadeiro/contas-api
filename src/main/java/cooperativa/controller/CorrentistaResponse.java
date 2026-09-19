package cooperativa.controller;

import cooperativa.model.Correntista;

public class CorrentistaResponse {

    private Long id;
    private String nome;
    private String documento;
    private String email;
    private String telefone;

    public CorrentistaResponse(Correntista correntista) {
        this.id = correntista.getId();
        this.nome = correntista.getNome();
        this.documento = correntista.getDocumentoCPFouCNPJ();
        this.email = correntista.getEmail();
        this.telefone = correntista.getTelefone();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}
