package com.thiago.minhasfinancas.repository;

import com.thiago.minhasfinancas.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
}
