package com.elos.api.service.catalogo;

import com.elos.api.dto.catalogo.ServicoRequest;
import com.elos.api.dto.catalogo.ServicoResponse;
import com.elos.api.entity.catalogo.Servico;
import com.elos.api.repository.catalogo.ServicoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.spi.ServiceRegistry;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

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

    public ServicoResponse criar(ServicoRequest servico) {
        Servico servicoObj = Servico.builder()
                .idTipoServico(servico.idTipoServico())
                .idPrestador(servico.idPrestador())
                .valor(servico.valor())
                .descricao(servico.descricao())
                .status(servico.status())
                .build();
        servicoObj.setIdServico(null);
        Servico salvo = servicoRepository.save(servicoObj);
        return ServicoResponse.criar(salvo);
    }

    public ServicoResponse atualizar(Long id, ServicoRequest dadosAtualizados) {
        Servico servicoExistente = buscarPorId(id);

        servicoExistente.setIdTipoServico(
                dadosAtualizados.idTipoServico()
        );

        servicoExistente.setIdPrestador(
                dadosAtualizados.idPrestador()
        );

        servicoExistente.setDescricao(
                dadosAtualizados.descricao()
        );

        servicoExistente.setValor(
                dadosAtualizados.valor()
        );

        servicoExistente.setStatus(
                dadosAtualizados.status()
        );

        Servico salvo = servicoRepository.save(servicoExistente);
        return ServicoResponse.criar(salvo);
    }

    public void excluir(Long id) {
        Servico servico = buscarPorId(id);
        servicoRepository.delete(servico);
    }
}