package com.unip.pitstop.repository;

import com.unip.pitstop.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    @Query("SELECT p FROM Peca p WHERE p.idPeca = :idPeca")
    Optional<Peca> findPecaById(@Param("idPeca") Long idPeca);
}
