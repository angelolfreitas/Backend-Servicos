package com.elos.comunicacao_service.service;

import com.elos.comunicacao_service.chat.Anexo;
import com.elos.comunicacao_service.chat.ContratacaoRef;
import com.elos.comunicacao_service.chat.Mensagem;
import com.elos.comunicacao_service.dto.AnexoResponse;
import com.elos.comunicacao_service.dto.MensagemResponse;
import com.elos.comunicacao_service.repository.AnexoRepository;
import com.elos.comunicacao_service.repository.ContratacaoRefRepository;
import com.elos.comunicacao_service.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final Set<String> ATIVOS = Set.of("NEGOCIANDO", "AGUARDANDO_PAGAMENTO", "EM_EXECUCAO");
    private static final int MAX_TEXTO = 1000;
    private static final Map<String, String> EXTENSOES = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp", "application/pdf", "pdf");

    private final ContratacaoRefRepository refs;
    private final MensagemRepository mensagens;
    private final AnexoRepository anexos;
    private final SimpMessagingTemplate broker;
    private final TransactionTemplate tx;

    @Value("${app.upload-dir}")
    private String uploadDir;

    public MensagemResponse enviarTexto(Long idContratacao, Long idUsuario, String texto) {
        if (texto == null || texto.isBlank())
            throw erro(HttpStatus.BAD_REQUEST, "Mensagem vazia");
        if (texto.length() > MAX_TEXTO)
            throw erro(HttpStatus.BAD_REQUEST, "Máximo de " + MAX_TEXTO + " caracteres");
        exigirParticipante(idContratacao, idUsuario, true);

        MensagemResponse r = montar(mensagens.save(nova(idContratacao, idUsuario, texto.strip())), List.of());
        broadcast(r);
        return r;
    }

    public MensagemResponse enviarAnexo(Long idContratacao, Long idUsuario,
                                        MultipartFile arquivo, String texto) throws IOException {
        exigirParticipante(idContratacao, idUsuario, true);
        String extensao = EXTENSOES.get(arquivo.getContentType());
        if (extensao == null)
            throw erro(HttpStatus.BAD_REQUEST, "Use JPEG, PNG, WEBP ou PDF");
        if (texto != null && texto.length() > MAX_TEXTO)
            throw erro(HttpStatus.BAD_REQUEST, "Máximo de " + MAX_TEXTO + " caracteres");

        Path dir = Path.of(uploadDir);
        Files.createDirectories(dir);
        Path destino = dir.resolve(UUID.randomUUID() + "." + extensao);
        arquivo.transferTo(destino);

        MensagemResponse r;
        try {
            r = tx.execute(status -> {
                Mensagem m = mensagens.save(nova(idContratacao, idUsuario,
                        (texto == null || texto.isBlank()) ? null : texto.strip()));
                Anexo a = new Anexo();
                a.setIdMensagem(m.getIdMensagem());
                a.setArquivo(destino.getFileName().toString());
                a.setTipoArquivo(arquivo.getContentType());
                return montar(m, List.of(anexos.save(a)));
            });
        } catch (RuntimeException e) {
            Files.deleteIfExists(destino);
            throw e;
        }
        broadcast(r);
        return r;
    }

    public List<MensagemResponse> historico(Long idContratacao, Long idUsuario, boolean admin) {
        if (!admin) exigirParticipante(idContratacao, idUsuario, false);
        List<Mensagem> lista = mensagens.findByIdContratacaoOrderByDataHoraAscIdMensagemAsc(idContratacao);
        Map<Long, List<Anexo>> porMensagem = anexos
                .findByIdMensagemIn(lista.stream().map(Mensagem::getIdMensagem).toList())
                .stream().collect(Collectors.groupingBy(Anexo::getIdMensagem));
        return lista.stream()
                .map(m -> montar(m, porMensagem.getOrDefault(m.getIdMensagem(), List.of())))
                .toList();
    }

    public Resource arquivo(Long idAnexo, Long idUsuario, boolean admin) {
        Anexo a = anexos.findById(idAnexo)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Anexo não encontrado"));
        Mensagem m = mensagens.findById(a.getIdMensagem())
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Mensagem não encontrada"));
        if (!admin) exigirParticipante(m.getIdContratacao(), idUsuario, false);
        return new FileSystemResource(Path.of(uploadDir).resolve(a.getArquivo()));
    }

    public void autorizarInscricao(Long idContratacao, Long idUsuario) {
        exigirParticipante(idContratacao, idUsuario, false);
    }

    // ---- auxiliares ----
    private void exigirParticipante(Long idContratacao, Long idUsuario, boolean exigirAtivo) {
        ContratacaoRef ref = refs.findById(idContratacao)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Chat indisponível"));
        if (!ref.getIdCliente().equals(idUsuario) && !ref.getIdPrestador().equals(idUsuario))
            throw erro(HttpStatus.FORBIDDEN, "Você não participa desta contratação");
        if (exigirAtivo && !ATIVOS.contains(ref.getStatus()))
            throw erro(HttpStatus.CONFLICT, "Chat encerrado");
    }

    private Mensagem nova(Long idContratacao, Long idUsuario, String texto) {
        Mensagem m = new Mensagem();
        m.setIdContratacao(idContratacao);
        m.setIdUsuarioRemetente(idUsuario);
        m.setMensagem(texto);
        m.setDataHora(LocalDateTime.now());
        return m;
    }

    private MensagemResponse montar(Mensagem m, List<Anexo> lista) {
        return new MensagemResponse(m.getIdMensagem(), m.getIdContratacao(), m.getIdUsuarioRemetente(),
                m.getMensagem(), m.getDataHora(),
                lista.stream().map(a -> new AnexoResponse(a.getIdAnexo(), a.getTipoArquivo(),
                        "/chats/anexos/" + a.getIdAnexo() + "/arquivo")).toList());
    }

    private void broadcast(MensagemResponse r) {
        broker.convertAndSend("/topic/contratacao/" + r.idContratacao(), r);
    }

    private ResponseStatusException erro(HttpStatus status, String msg) {
        return new ResponseStatusException(status, msg);
    }
}