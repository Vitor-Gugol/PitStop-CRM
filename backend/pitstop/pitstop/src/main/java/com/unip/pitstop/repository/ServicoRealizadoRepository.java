package com.unip.pitstop.repository;

import com.unip.pitstop.model.ServicoRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServicoRealizadoRepository extends JpaRepository<ServicoRealizado, Long> {

    List<ServicoRealizado> findByMecanicoId(Long mecanicoId);

    @Query("SELECT sr FROM ServicoRealizado sr " +
            "JOIN sr.ordemServico os " +
            "WHERE sr.mecanicoId = :mecanicoId " +
            "AND os.dataEntrada >= :dataInicio " +
            "AND os.dataEntrada < :dataFim")
    List<ServicoRealizado> findByMecanicoIdAndPeriodo(
            @Param("mecanicoId") Long mecanicoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}
