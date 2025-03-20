package com.unip.pitstop.repository;

import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.model.Peca;
import com.unip.pitstop.model.PecaUtilizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PecaUtilizadaRepository extends JpaRepository<PecaUtilizada, Long> {
    Optional<PecaUtilizada> findByOrdemServicoAndPeca(OrdemServico ordemServico, Peca peca);
}
