package com.elos.comunicacao_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MensagemResponse(Long id, Long idContratacao, Long idRemetente, String texto,
                               LocalDateTime dataHora, List<AnexoResponse> anexos) {}