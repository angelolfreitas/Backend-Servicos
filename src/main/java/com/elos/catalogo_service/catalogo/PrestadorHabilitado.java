package com.elos.catalogo_service.catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prestador_habilitado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorHabilitado {
    @Id
    private Long idPrestador;

    @Column(name = "id_usuario", unique = true)
    private Long idUsuario;
}