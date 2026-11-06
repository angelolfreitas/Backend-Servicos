package com.elos.usuario_service.service;

import com.elos.usuario_service.dto.DocumentoResponse;
import com.elos.usuario_service.repository.DocumentoRepository;
import com.elos.usuario_service.repository.PrestadorRepository;
import com.elos.usuario_service.usuario.Documento;
import com.elos.usuario_service.usuario.Prestador;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoService {
    private static final Map<String, String> EXTENSOES = Map.of(
            "application/pdf", "pdf", "image/jpeg", "jpg", "image/png", "png");
    private static final Set<String> TIPOS = Set.of(
            "IDENTIDADE", "COMPROVANTE_RESIDENCIA", "CNPJ", "CERTIFICACAO");

    private final DocumentoRepository documentos;
    private final PrestadorRepository prestadores;

    @Value("${app.upload-dir}")
    private String uploadDir;

    public DocumentoResponse enviar(Long idUsuario, String tipo, MultipartFile arquivo) throws IOException {
        Prestador p = prestadores.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Envie a candidatura primeiro"));
        if (!"PENDENTE".equals(p.getStatusAprovacao()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidatura não está em análise");
        if (!TIPOS.contains(tipo))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo inválido. Use " + TIPOS);
        String extensao = EXTENSOES.get(arquivo.getContentType());
        if (extensao == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use PDF, JPEG ou PNG");

        Path dir = Path.of(uploadDir);
        Files.createDirectories(dir);
        Path destino = dir.resolve(UUID.randomUUID() + "." + extensao);
        arquivo.transferTo(destino);

        Documento d = new Documento();
        d.setIdPrestador(p.getIdPrestador());
        d.setTipoDocumento(tipo);
        d.setAnexo(destino.getFileName().toString());
        d.setDataEnvio(LocalDate.now());
        d.setStatusAnalise("PENDENTE");
        return DocumentoResponse.criar(documentos.save(d));
    }

    public List<DocumentoResponse> meus(Long idUsuario) {
        Prestador p = prestadores.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sem candidatura"));
        return doPrestador(p.getIdPrestador());
    }

    public List<DocumentoResponse> doPrestador(Long idPrestador) {
        return documentos.findByIdPrestador(idPrestador).stream().map(DocumentoResponse::criar).toList();
    }

    public DocumentoResponse analisar(Long idDocumento, String status) {
        if (!"APROVADO".equals(status) && !"REJEITADO".equals(status))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status deve ser APROVADO ou REJEITADO");
        Documento d = buscar(idDocumento);
        d.setStatusAnalise(status);
        return DocumentoResponse.criar(documentos.save(d));
    }

    public Resource arquivo(Long idDocumento) {
        return new FileSystemResource(Path.of(uploadDir).resolve(buscar(idDocumento).getAnexo()));
    }

    private Documento buscar(Long id) {
        return documentos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));
    }
}