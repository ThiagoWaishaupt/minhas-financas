package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.enums.ReleaseStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReleaseService {
    Release save(Release release);

    Release update(Release release);

    void delete(Release release);

    List<Release> search(Release release);

    void validate(Release release);

    Optional<Release> searchReleaseById(Long idRelease);

    BigDecimal getBalance(Long userId);
}
