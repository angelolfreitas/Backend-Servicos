package com.elos.catalogo_service;



import com.elos.catalogo_service.catalogo.Servico;
import com.elos.catalogo_service.dto.catalogo.ServicoRequest;
import com.elos.catalogo_service.dto.catalogo.ServicoResponse;
import com.elos.catalogo_service.repository.PrestadorHabilitadoRepository;
import com.elos.catalogo_service.repository.ServicoRepository;
import com.elos.catalogo_service.repository.TipoServicoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final PrestadorHabilitadoRepository prestadorHabilitadoRepository;
    private final TipoServicoRepository tipoServicoRepository;

    public List<ServicoResponse> listarTodos() {
        return servicoRepository.findAll().stream().map(ServicoResponse::criar).toList();
    }

    private Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Serviço não encontrado: " + id
                ));
    }
    public ServicoResponse wrapPorId(Long id) {
        return ServicoResponse.criar(
                servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Serviço não encontrado: " + id
                )));
    }

    private Long idPrestadorDe(Long idUsuario) {
        return prestadorHabilitadoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Prestador não aprovado"))
                .getIdPrestador();
    }

    private void exigirDono(Servico servico, Long idUsuario) {
        if (!servico.getIdPrestador().equals(idPrestadorDe(idUsuario)))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Serviço de outro prestador");
    }

    public ServicoResponse criar(ServicoRequest req, Long idUsuario) {
        tipoServicoRepository.findById(req.idTipoServico())
                .filter(t -> "ATIVO".equals(t.getStatusAtivo()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de serviço inválido"));
        Servico servico = Servico.builder()
                .idTipoServico(req.idTipoServico())
                .idPrestador(idPrestadorDe(idUsuario))
                .descricao(req.descricao())
                .valor(req.valor())
                .status(req.status())
                .build();
        return ServicoResponse.criar(servicoRepository.save(servico));
    }

    public ServicoResponse atualizar(Long id, ServicoRequest dados, Long idUsuario) {
        Servico servico = buscarPorId(id);
        exigirDono(servico, idUsuario);
        servico.setIdTipoServico(dados.idTipoServico());
        servico.setDescricao(dados.descricao());
        servico.setValor(dados.valor());
        servico.setStatus(dados.status());
        return ServicoResponse.criar(servicoRepository.save(servico));
    }

    public void excluir(Long id, Long idUsuario) {
        Servico servico = buscarPorId(id);
        exigirDono(servico, idUsuario);
        servicoRepository.delete(servico);
    }
}