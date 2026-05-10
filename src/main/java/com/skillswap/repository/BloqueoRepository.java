package com.skillswap.repository;

import com.skillswap.model.Bloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueoRepository extends JpaRepository<Bloqueo, Long> {
    boolean existsByIdBloqueadorAndIdBloqueado(Long idBloqueador, Long idBloqueado);
}

