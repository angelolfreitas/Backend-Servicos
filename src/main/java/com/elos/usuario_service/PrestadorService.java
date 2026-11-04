package com.elos.usuario_service;

import com.elos.usuario_service.dto.CandidaturaRequest;
import com.elos.usuario_service.dto.PrestadorAprovado;
import com.elos.usuario_service.dto.PrestadorRejeitado;
import com.elos.usuario_service.dto.PrestadorResponse;
import com.elos.usuario_service.repository.DocumentoRepository;
import com.elos.usuario_service.repository.PrestadorRepository;
import com.elos.usuario_service.usuario.Documento;
import com.elos.usuario_service.usuario.Prestador;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PrestadorService {
    private final PrestadorRepository repository;
    private final DocumentoRepository documentos;
    private final ApplicationEventPublisher events;

    @Transactional
    public PrestadorResponse candidatar(Long idUsuario, CandidaturaRequest req) {
        Prestador p = repository.findByIdUsuario(idUsuario).orElseGet(Prestador::new);
        if (p.getIdPrestador() != null && !"REJEITADO".equals(p.getStatusAprovacao()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já enviou uma candidatura");

        boolean pf = "PF".equals(req.tipoPrestador());
        boolean pj = "PJ".equals(req.tipoPrestador());
        if (!pf && !pj)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoPrestador deve ser PF ou PJ");
        if (pf && (req.cpf() == null || req.cpf().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF obrigatório para PF");
        if (pj && (req.cnpj() == null || req.cnpj().isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ obrigatório para PJ");

        p.setIdUsuario(idUsuario);
        p.setTipoPrestador(req.tipoPrestador());
        p.setCpf(pf ? req.cpf() : null);
        p.setCnpj(pj ? req.cnpj() : null);
        p.setStatusAprovacao("PENDENTE");
        return PrestadorResponse.criar(repository.save(p));
    }

    public PrestadorResponse minhaCandidatura(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario).map(PrestadorResponse::criar)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sem candidatura"));
    }

    public List<PrestadorResponse> pendentes() {
        return repository.findByStatusAprovacao("PENDENTE").stream().map(PrestadorResponse::criar).toList();
    }

    @Transactional
    public void aprovar(Long id) {
        Prestador p = buscar(id);
        if ("APROVADO".equals(p.getStatusAprovacao()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Prestador já aprovado");
        List<Documento> docs = documentos.findByIdPrestador(id);
        boolean temAprovado = docs.stream().anyMatch(d -> "APROVADO".equals(d.getStatusAnalise()));
        boolean temPendente = docs.stream().anyMatch(d -> "PENDENTE".equals(d.getStatusAnalise()));
        if (!temAprovado || temPendente)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Precisa de ao menos um documento aprovado e nenhum pendente de análise");
        p.setStatusAprovacao("APROVADO");
        repository.save(p);
        events.publishEvent(new PrestadorAprovado(p.getIdPrestador(), p.getIdUsuario()));
    }

    @Transactional
    public void rejeitar(Long id) {
        Prestador p = buscar(id);
        if ("REJEITADO".equals(p.getStatusAprovacao()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Prestador já rejeitado");
        p.setStatusAprovacao("REJEITADO");
        repository.save(p);
        events.publishEvent(new PrestadorRejeitado(p.getIdPrestador(), p.getIdUsuario()));
    }

    private Prestador buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestador não encontrado"));
    }
}