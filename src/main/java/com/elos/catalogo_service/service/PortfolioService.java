package com.elos.catalogo_service.service;

import com.elos.catalogo_service.catalogo.FotoPortfolio;
import com.elos.catalogo_service.catalogo.Portfolio;
import com.elos.catalogo_service.dto.catalogo.FotoResponse;
import com.elos.catalogo_service.dto.catalogo.PortfolioRequest;
import com.elos.catalogo_service.dto.catalogo.PortfolioResponse;
import com.elos.catalogo_service.repository.FotoPortfolioRepository;
import com.elos.catalogo_service.repository.PortfolioRepository;
import com.elos.catalogo_service.repository.PrestadorHabilitadoRepository;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private static final Map<String, String> EXTENSOES = Map.of(
            "image/jpeg", "jpg", "image/png", "png", "image/webp", "webp");

    private final PortfolioRepository portfolios;
    private final FotoPortfolioRepository fotos;
    private final PrestadorHabilitadoRepository prestadores;

    @Value("${app.upload-dir}")
    private String uploadDir;

    public PortfolioResponse salvar(Long idUsuario, PortfolioRequest req) {
        Long idPrestador = idPrestadorDe(idUsuario);
        Portfolio p = portfolios.findByIdPrestador(idPrestador).orElseGet(() -> {
            Portfolio novo = new Portfolio();
            novo.setIdPrestador(idPrestador);
            return novo;
        });
        p.setFotoPerfil(req.fotoPerfil());
        p.setCurriculo(req.curriculo());
        p.setCidade(req.cidade());
        p.setRegiao(req.regiao());
        p.setDescricao(req.descricao());
        return montar(portfolios.save(p));
    }

    public PortfolioResponse meu(Long idUsuario) {
        return montar(portfolioDe(idPrestadorDe(idUsuario)));
    }

    public PortfolioResponse doPrestador(Long idPrestador) {
        if (!prestadores.existsById(idPrestador))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestador não encontrado");
        return montar(portfolioDe(idPrestador));
    }

    public FotoResponse adicionarFoto(Long idUsuario, MultipartFile arquivo, String descricao)
            throws IOException {
        Portfolio p = portfolioDe(idPrestadorDe(idUsuario));
        String extensao = EXTENSOES.get(arquivo.getContentType());
        if (extensao == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use JPEG, PNG ou WEBP");

        Path dir = Path.of(uploadDir);
        Files.createDirectories(dir);
        Path destino = dir.resolve(UUID.randomUUID() + "." + extensao);
        arquivo.transferTo(destino);

        FotoPortfolio f = new FotoPortfolio();
        f.setIdPortfolio(p.getIdPortfolio());
        f.setAnexo(destino.getFileName().toString());
        f.setDescricao(descricao);
        return paraResponse(fotos.save(f));
    }

    public void removerFoto(Long idUsuario, Long idFoto) throws IOException {
        FotoPortfolio f = fotoDe(idFoto);
        Portfolio p = portfolios.findById(f.getIdPortfolio()).orElseThrow();
        if (!p.getIdPrestador().equals(idPrestadorDe(idUsuario)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Foto de outro prestador");
        Files.deleteIfExists(Path.of(uploadDir).resolve(f.getAnexo()));
        fotos.delete(f);
    }

    public Resource arquivo(Long idFoto) {
        FotoPortfolio f = fotoDe(idFoto);
        return new FileSystemResource(Path.of(uploadDir).resolve(f.getAnexo()));
    }

    private Long idPrestadorDe(Long idUsuario) {
        return prestadores.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Prestador não aprovado"))
                .getIdPrestador();
    }

    private Portfolio portfolioDe(Long idPrestador) {
        return portfolios.findByIdPrestador(idPrestador)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfólio não cadastrado"));
    }

    private FotoPortfolio fotoDe(Long idFoto) {
        return fotos.findById(idFoto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto não encontrada"));
    }

    private FotoResponse paraResponse(FotoPortfolio f) {
        return new FotoResponse(f.getIdFotoPortfolio(), f.getDescricao(),
                "/portfolio/fotos/" + f.getIdFotoPortfolio() + "/arquivo");
    }

    private PortfolioResponse montar(Portfolio p) {
        List<FotoResponse> lista = fotos.findByIdPortfolio(p.getIdPortfolio())
                .stream().map(this::paraResponse).toList();
        return new PortfolioResponse(p.getIdPrestador(), p.getFotoPerfil(), p.getCurriculo(),
                p.getCidade(), p.getRegiao(), p.getDescricao(), lista);
    }
}