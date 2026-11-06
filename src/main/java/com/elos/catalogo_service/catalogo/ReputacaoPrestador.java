package com.elos.catalogo_service.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "reputacao_prestador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReputacaoPrestador {
    @Id
    private Long idUsuario;
    @Column(precision = 3, scale = 2)
    private BigDecimal media;
    private Long total;
}
