package com.unip.pitstop.repository;

import com.unip.pitstop.model.OrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
    List<OrdemServico> findByStatus(String status);

    List<OrdemServico> findByStatusNotIn(List<String> statuses);
    Page<OrdemServico> findAllByOrderByDataEntradaDesc(Pageable pageable);
    List<OrdemServico> findByStatusIgnoreCase(String status);

}