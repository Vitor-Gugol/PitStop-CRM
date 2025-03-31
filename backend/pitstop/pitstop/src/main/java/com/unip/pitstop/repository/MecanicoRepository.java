package com.unip.pitstop.repository;


import com.unip.pitstop.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
}