package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.enums.ReleaseStatus;

import java.util.List;

public interface ReleaseService {
    Release save(Release release);
    Release update(Release release);
    void delete(Release release);
    List<Release> search(Release release);
    void updateStatus(Release release, ReleaseStatus status);
    void validate(Release release);
}
