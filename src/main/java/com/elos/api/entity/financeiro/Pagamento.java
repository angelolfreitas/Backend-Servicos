package com.elos.api.entity.financeiro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Entidade do modelo ELOS. Relacionamentos mantidos como IDs nesta primeira etapa para facilitar futura divisão em microsserviços. */
@Setter
@Getter
@Entity
@Table(name = "pagamento")
public class Pagamento implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i\1_\2agamento")
    private Long idPagamento;

    @Column(name = "i\1_\2ontratacao")
    private Long idContratacao;

    @Column(name = "valo\1_\2otal")
    private BigDecimal valorTotal;

    @Column(name = "form\1_\2agamento")
    private String formaPagamento;

    @Column(name = "status")
    private String status;

    @Column(name = "data")
    private LocalDateTime data;

}
