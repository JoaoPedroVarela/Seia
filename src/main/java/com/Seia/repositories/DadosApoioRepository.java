package com.Seia.repositories;

import com.Seia.models.DadosApoio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DadosApoioRepository extends JpaRepository<DadosApoio, Long> {

    List<DadosApoio> findByUsuarioId(Long usuarioId);
}
