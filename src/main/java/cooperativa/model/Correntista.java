package cooperativa.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "correntista")
public class Correntista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String documentoCPFouCNPJ;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = true, length = 25 )
    private String telefone;

    @OneToMany(mappedBy = "correntista", cascade = CascadeType.ALL)
    private List<Conta> contas = new ArrayList<Conta>();

    public Correntista() {//O jpa deixa obrigado ter esse
    }

    public Correntista(String nome, String documentoCPFouCNPJ, String email, String telefone) {
        this.nome = nome;
        this.documentoCPFouCNPJ = documentoCPFouCNPJ;
        this.email = email;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumentoCPFouCNPJ() {
        return documentoCPFouCNPJ;
    }

    public void setDocumentoCPFouCNPJ(String documentoCPFouCNPJ) {
        this.documentoCPFouCNPJ = documentoCPFouCNPJ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Conta> getContas() {
        return contas;
    }

}
