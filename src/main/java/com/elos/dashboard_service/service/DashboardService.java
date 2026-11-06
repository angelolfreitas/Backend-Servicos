package com.elos.dashboard_service.service;

import com.elos.dashboard_service.dto.FaturamentoDiaResponse;
import com.elos.dashboard_service.dto.ResumoResponse;
import com.elos.dashboard_service.repository.ComissaoLiberadaRepository;
import com.elos.dashboard_service.repository.ContratacaoStatusRepository;
import com.elos.dashboard_service.repository.FaturamentoDia;
import com.elos.dashboard_service.repository.TicketStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ContratacaoStatusRepository contratacoes;
    private final ComissaoLiberadaRepository comissoes;
    private final TicketStatusRepository tickets;

    public ResumoResponse resumo() {
        long total = contratacoes.count();
        long finalizadas = contratacoes.countByStatus("CONCLUIDA");
        BigDecimal taxa = total == 0
                ? BigDecimal.ZERO.setScale(2)
                : BigDecimal.valueOf(finalizadas * 100).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        return new ResumoResponse(total, finalizadas, taxa, tickets.contarAbertos(),
                comissoes.somarDoDia(LocalDate.now()), comissoes.somarTotal());
    }

    public List<FaturamentoDiaResponse> faturamento(int dias) {
        if (dias < 1 || dias > 365)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dias deve ficar entre 1 e 365");

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusDays(dias - 1L);

        Map<LocalDate, FaturamentoDia> existentes = new HashMap<>();
        comissoes.porDia(inicio).forEach(f -> existentes.put(f.getData(), f));

        List<FaturamentoDiaResponse> serie = new ArrayList<>(dias);
        for (LocalDate d = inicio; !d.isAfter(hoje); d = d.plusDays(1)) {
            FaturamentoDia f = existentes.get(d);
            serie.add(f == null
                    ? new FaturamentoDiaResponse(d, BigDecimal.ZERO.setScale(2), 0)
                    : new FaturamentoDiaResponse(d, f.getValor(), f.getQtd()));
        }
        return serie;
    }
}