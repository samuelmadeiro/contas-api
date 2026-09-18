package cooperativa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String numero;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo = new BigDecimal("0.00"); //começa zerado

    @ManyToOne
    @JoinColumn(name = "ID_correntista", nullable = false)
    private Correntista correntista;

    @OneToMany(mappedBy = "conta")
    private List<Transacao> transacoes = new ArrayList<Transacao>();

    public Conta(){}

    public Conta(String numero, Correntista correntista) {
        this.numero = numero;
        this.correntista = correntista;
    }

    public abstract TipoDeConta getTipo();

    public abstract void sacar(BigDecimal valor);

    protected void validarTransacao(BigDecimal valor){
        if(valor == null || valor.compareTo(BigDecimal.ZERO) <=0){
            throw new RegraDeNegocioException("A transação deve ter um valor maior que 0");
        }
    }

    public void depositar(BigDecimal valor){
        validarTransacao(valor);
        this.saldo = this.saldo.add(valor);
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Correntista getCorrentista() {
        return correntista;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }
    public void setCorrentista(Correntista correntista){
        this.correntista = correntista;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    protected void setSaldo(BigDecimal saldo){
        this.saldo = saldo;
    }
}
