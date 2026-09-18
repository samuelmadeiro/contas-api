package cooperativa.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDeTransacao tipo;

    @Column(nullable = false, precision =19, scale = 2)
    private BigDecimal valor;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name ="contaid", nullable = false)
    private Conta conta;

    public Transacao(){
    }

    public Transacao(TipoDeTransacao tipo, BigDecimal valor, Conta conta) {
        this.tipo = tipo;
        this.valor = valor;
        this.conta = conta;
        this.data = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public TipoDeTransacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Conta getConta() {
        return conta;
    }
}
