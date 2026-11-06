package com.elos.api_gateway;


import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Component
public class ContasBloqueadas {
    private final Set<String> ids = ConcurrentHashMap.newKeySet();

    public void aplicar(Long idUsuario, String acao) {
        if (idUsuario == null || acao == null) return;
        String id = idUsuario.toString();
        switch (acao) {
            case "SUSPENSA", "BANIDA" -> ids.add(id);
            case "REATIVADA" -> ids.remove(id);
            default -> { }
        }
    }

    public boolean contem(String idUsuario) {
        return idUsuario != null && ids.contains(idUsuario);
    }
}