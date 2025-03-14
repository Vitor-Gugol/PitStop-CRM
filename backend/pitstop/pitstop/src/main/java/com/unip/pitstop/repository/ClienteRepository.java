package com.unip.pitstop.repository;

import com.unip.pitstop.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
    public interface ClienteRepository extends JpaRepository<Cliente, Long>{

    }



