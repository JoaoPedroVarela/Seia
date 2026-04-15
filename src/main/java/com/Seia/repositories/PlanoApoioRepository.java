package com.Seia.repositories;

import com.Seia.models.PlanosApoio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoApoioRepository extends JpaRepository<PlanosApoio, Long> {

    List<PlanosApoio> findByUsuarioId(Long usuarioId);

    Optional<PlanosApoio> findByDadosApoioId(Long dadosApoioId);
}
