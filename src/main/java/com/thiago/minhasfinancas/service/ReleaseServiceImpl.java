package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.enums.ReleaseStatus;
import com.thiago.minhasfinancas.model.enums.ReleaseType;
import com.thiago.minhasfinancas.repository.ReleaseRepository;
import com.thiago.minhasfinancas.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReleaseServiceImpl implements ReleaseService{

    private ReleaseRepository releaseRepository;

    @Override
    @Transactional
    public Release save(Release release) {
        validate(release);
        release.setReleaseStatus(ReleaseStatus.PENDENTE);
        return releaseRepository.save(release);
    }

    @Override
    @Transactional
    public Release update(Release release) {
        Objects.requireNonNull(release.getId());
        validate(release);
        return releaseRepository.save(release); //save method update fields too
    }

    @Override
    @Transactional
    public void delete(Release release) {
        Objects.requireNonNull(release.getId());
        releaseRepository.delete(release);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Release> search(Release release) {

        Example example = Example.of(release,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return releaseRepository.findAll(example);
    }

    @Override
    public void validate(Release release) {
        if(Objects.isNull(release.getDescription()) || release.getDescription().trim().equals("")){
           throw new BusinessRuleException(Constants.INVALID_DESCRIPTION);
        }

        if(Objects.isNull(release.getMonth()) || release.getMonth() < 1 || release.getMonth() > 12){
            throw new BusinessRuleException(Constants.INVALID_MONTH);
        }

        if(Objects.isNull(release.getYear()) || release.getYear().toString().length() != 4){
            throw new BusinessRuleException(Constants.INVALID_YEAR);
        }

        if(Objects.isNull(release.getUser())){
            throw new BusinessRuleException(Constants.INVALID_USER);
        }

        if(Objects.isNull(release.getValue()) || release.getValue().compareTo(BigDecimal.ZERO) < 1){ //compare with BigDecimal
            throw new BusinessRuleException(Constants.INVALID_VALUE);
        }

        if(Objects.isNull(release.getReleaseType())){
            throw new BusinessRuleException(Constants.INVALID_RELEASE_TYPE);
        }
    }

    @Override
    public Optional<Release> searchReleaseById(Long idRelease) {
        return releaseRepository.findById(idRelease);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long userId) {
        BigDecimal receita = releaseRepository.getUserBalance(userId, ReleaseType.RECEITA);
        BigDecimal despesa = releaseRepository.getUserBalance(userId, ReleaseType.DESPESA);

        if (Objects.isNull(receita))
            receita = BigDecimal.ZERO;
        if(Objects.isNull(despesa))
            despesa = BigDecimal.ZERO;

        return receita.subtract(despesa);
    }
}
