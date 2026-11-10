package com.elos.usuario_service.kafka;

import com.elos.usuario_service.dto.ContaModerada;
import com.elos.usuario_service.repository.UsuarioRepository;
import com.elos.usuario_service.usuario.estado.StatusConta;
import com.elos.usuario_service.usuario.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/// Consumer deste microsservice. Ouve do microsservice de suporte os banimentos, reativacoes e suspensoes de usuarios.
@Component
@RequiredArgsConstructor
public class ContaListener {
    private final UsuarioRepository usuarios;

    @KafkaListener(topics = "conta-moderada", groupId = "usuario",
            properties = "spring.json.value.default.type=com.elos.usuario_service.dto.ContaModerada")
    @Transactional
    public void ouvir(ContaModerada e) {
        usuarios.findById(e.idUsuario()).ifPresent(u -> {
            if (u.getRole() == Role.ADMIN) return;
            u.setStatusConta(switch (e.acao()) {
                case "SUSPENSA" -> StatusConta.SUSPENSO;
                case "BANIDA" -> StatusConta.BANIDO;
                case "REATIVADA" -> StatusConta.ATIVO;
                default -> u.getStatusConta();
            });
            usuarios.save(u);
        });
    }
}