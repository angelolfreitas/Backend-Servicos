package com.elos.suporte_service.service;

import com.elos.suporte_service.EventPublisher;
import com.elos.suporte_service.dto.MensagemTicketResponse;
import com.elos.suporte_service.dto.TicketAtualizado;
import com.elos.suporte_service.dto.TicketRequest;
import com.elos.suporte_service.dto.TicketResponse;
import com.elos.suporte_service.repository.MensagemTicketRepository;
import com.elos.suporte_service.repository.TicketRepository;
import com.elos.suporte_service.suporte.MensagemTicket;
import com.elos.suporte_service.suporte.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final Set<String> ABERTOS = Set.of("ABERTO", "EM_ATENDIMENTO");
    private static final Set<String> STATUS = Set.of("ABERTO", "EM_ATENDIMENTO", "ENCERRADO");

    private final TicketRepository tickets;
    private final MensagemTicketRepository mensagens;
    private final ApplicationEventPublisher events;

    @Transactional
    public TicketResponse criar(Long idUsuario, TicketRequest req) {
        Ticket t = new Ticket();
        t.setIdUsuario(idUsuario);
        t.setCategoriaProblema(req.categoriaProblema());
        t.setIdContratacao(req.idContratacao());
        t.setStatus("ABERTO");
        t.setDataAbertura(LocalDateTime.now());
        t = tickets.save(t);
        t.setProtocolo("ELOS-%d-%06d".formatted(t.getDataAbertura().getYear(), t.getIdTicket()));
        t = tickets.save(t);
        adicionar(t, idUsuario, req.mensagem());
        publicar(t);
        return detalhe(t);
    }

    @Transactional
    public TicketResponse responderAdmin(Long id, Long idAdmin, String texto) {
        Ticket t = buscar(id);
        exigirAberto(t);
        if (t.getIdAdministrador() == null) t.setIdAdministrador(idAdmin);
        t.setStatus("EM_ATENDIMENTO");
        tickets.save(t);
        adicionar(t, idAdmin, texto);
        publicar(t);
        return detalhe(t);
    }

    @Transactional
    public TicketResponse encerrar(Long id, Long idAdmin) {
        Ticket t = buscar(id);
        exigirAberto(t);
        if (t.getIdAdministrador() == null) t.setIdAdministrador(idAdmin);
        t.setStatus("ENCERRADO");
        t.setDataFechamento(LocalDateTime.now());
        tickets.save(t);
        publicar(t);
        return detalhe(t);
    }

    private void adicionar(Ticket t, Long idRemetente, String texto) {
        MensagemTicket m = new MensagemTicket();
        m.setIdTicket(t.getIdTicket());
        m.setIdUsuarioRemetente(idRemetente);
        m.setMensagem(texto);
        m.setDataHora(LocalDateTime.now());
        mensagens.save(m);
    }

    private void publicar(Ticket t) {
        events.publishEvent(new TicketAtualizado(t.getIdTicket(), t.getStatus()));
    }




    public List<TicketResponse> meus(Long idUsuario) {
        return tickets.findByIdUsuarioOrderByIdTicketDesc(idUsuario).stream().map(this::resumo).toList();
    }

    public TicketResponse detalheDoUsuario(Long id, Long idUsuario) {
        return detalhe(doUsuario(id, idUsuario));
    }

    @Transactional
    public TicketResponse responderUsuario(Long id, Long idUsuario, String texto) {
        Ticket t = doUsuario(id, idUsuario);
        exigirAberto(t);
        adicionar(t, idUsuario, texto);
        return detalhe(t);
    }

    // ---- admin ----
    public List<TicketResponse> fila(String status) {
        if (status != null && !STATUS.contains(status))
            throw erro(HttpStatus.BAD_REQUEST, "status deve ser ABERTO, EM_ATENDIMENTO ou ENCERRADO");
        Set<String> filtro = status == null ? ABERTOS : Set.of(status);
        return tickets.findByStatusInOrderByIdTicketAsc(filtro).stream().map(this::resumo).toList();
    }

    public TicketResponse detalheAdmin(Long id) {
        return detalhe(buscar(id));
    }





    private Ticket buscar(Long id) {
        return tickets.findById(id).orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Ticket não encontrado"));
    }

    private Ticket doUsuario(Long id, Long idUsuario) {
        Ticket t = buscar(id);
        if (!t.getIdUsuario().equals(idUsuario)) throw erro(HttpStatus.FORBIDDEN, "Ticket de outro usuário");
        return t;
    }

    private void exigirAberto(Ticket t) {
        if ("ENCERRADO".equals(t.getStatus()))
            throw erro(HttpStatus.CONFLICT, "Ticket encerrado. Abra um novo chamado");
    }

    private TicketResponse resumo(Ticket t) {
        return montar(t, List.of());
    }

    private TicketResponse detalhe(Ticket t) {
        List<MensagemTicketResponse> lista = mensagens
                .findByIdTicketOrderByDataHoraAscIdMensagemTicketAsc(t.getIdTicket()).stream()
                .map(m -> new MensagemTicketResponse(m.getIdMensagemTicket(), m.getIdUsuarioRemetente(),
                        m.getIdUsuarioRemetente().equals(t.getIdUsuario()) ? "USUARIO" : "ADMINISTRADOR",
                        m.getMensagem(), m.getDataHora()))
                .toList();
        return montar(t, lista);
    }

    private TicketResponse montar(Ticket t, List<MensagemTicketResponse> lista) {
        return new TicketResponse(t.getIdTicket(), t.getProtocolo(), t.getIdUsuario(), t.getIdAdministrador(),
                t.getCategoriaProblema(), t.getIdContratacao(), t.getStatus(), t.getDataAbertura(),
                t.getDataFechamento(), lista);
    }

    private ResponseStatusException erro(HttpStatus s, String msg) {
        return new ResponseStatusException(s, msg);
    }

    @Transactional
    public TicketResponse abrir(Long idUsuario, TicketRequest req) {
        Ticket t = new Ticket();
        t.setIdUsuario(idUsuario);
        t.setCategoriaProblema(req.categoriaProblema());
        t.setIdContratacao(req.idContratacao());
        t.setStatus("ABERTO");
        t.setDataAbertura(LocalDateTime.now());
        t = tickets.save(t);
        t.setProtocolo("ELOS-%d-%06d".formatted(t.getDataAbertura().getYear(), t.getIdTicket()));
        t = tickets.save(t);
        adicionar(t, idUsuario, req.mensagem());
        publicar(t);
        return detalhe(t);
    }
}