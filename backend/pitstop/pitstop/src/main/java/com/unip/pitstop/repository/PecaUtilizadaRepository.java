package com.unip.pitstop.repository;

import com.unip.pitstop.model.PecaUtilizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PecaUtilizadaRepository extends JpaRepository<PecaUtilizada, Long> {
}
