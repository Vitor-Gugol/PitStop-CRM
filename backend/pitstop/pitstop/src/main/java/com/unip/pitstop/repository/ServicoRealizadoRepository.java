package com.unip.pitstop.repository;

import com.unip.pitstop.model.ServicoRealizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRealizadoRepository extends JpaRepository<ServicoRealizado, Long> {
}
