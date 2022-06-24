package com.thiago.minhasfinancas.service;

import com.thiago.minhasfinancas.exception.BusinessRuleException;
import com.thiago.minhasfinancas.model.Release;
import com.thiago.minhasfinancas.model.ReleaseDTO;
import com.thiago.minhasfinancas.model.User;
import com.thiago.minhasfinancas.model.enums.ReleaseStatus;
import com.thiago.minhasfinancas.model.enums.ReleaseType;
import com.thiago.minhasfinancas.repository.ReleaseRepository;
import com.thiago.minhasfinancas.util.Constants;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ReleaseServiceTest {

    @MockBean
    ReleaseRepository releaseRepository;

    @SpyBean
    ReleaseServiceImpl releaseService;

    @Test
    void saveReleaseWithSuccess(){

        Release release = createDefaultRelease();
        Release releaseResult = createDefaultRelease();
        releaseResult.setId(1L);
        releaseResult.setReleaseStatus(ReleaseStatus.PENDENTE);

        doNothing().when(releaseService).validate(release);

        when(releaseRepository.save(any(Release.class))).thenReturn(releaseResult);

        release = releaseService.save(release);

        Assertions.assertNotNull(release.getId());
        Assertions.assertNotNull(release.getReleaseStatus());
        Assertions.assertEquals(release.getReleaseStatus(), ReleaseStatus.PENDENTE);
    }

    @Test
    void saveReleaseWithError(){
        Release release = createDefaultRelease();

        doThrow(BusinessRuleException.class).when(releaseService).validate(release);

        Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.save(release));
        verify(releaseRepository, never()).save(release);
    }

    @Test
    void updateReleaseWithSuccess(){

        Release releaseResult = createDefaultRelease();
        releaseResult.setId(1L);
        releaseResult.setReleaseStatus(ReleaseStatus.PENDENTE);

        doNothing().when(releaseService).validate(releaseResult);

        when(releaseRepository.save(releaseResult)).thenReturn(releaseResult);

        releaseService.update(releaseResult);

        verify(releaseRepository, times(1)).save(releaseResult);
    }

    @Test
    void updateReleaseWithValidateError(){
        Release release = createDefaultRelease();
        release.setId(1L);

        doThrow(BusinessRuleException.class).when(releaseService).validate(release);

        Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.update(release));
        verify(releaseRepository, never()).save(release);
    }

    @Test
    void updateReleaseWithIdNullError(){
        Release release = createDefaultRelease();

        Assertions.assertThrows(NullPointerException.class, () -> releaseService.update(release));
        verify(releaseRepository, never()).save(release);
    }

    @Test
    void deleteResourceWithSuccess(){
        Release release = createDefaultRelease();
        release.setId(1L);

        releaseService.delete(release);

        verify(releaseRepository).delete(release);
    }

    @Test
    void deleteResourceWithIdNullError(){
        Release release = createDefaultRelease();

        Assertions.assertThrows(NullPointerException.class, () -> releaseService.delete(release));
        verify(releaseRepository, never()).delete(release);
    }

    @Test
    void searchReleaseWithSuccess(){
        Release releaseA = createDefaultRelease();
        Release releaseB = createDefaultRelease();
        Release releaseC = createDefaultRelease();

        List<Release> releaseList = Arrays.asList(releaseA, releaseB);

        when(releaseRepository.findAll(any(Example.class))).thenReturn(releaseList);

        List<Release> releaseListResult = releaseService.search(releaseC);

        Assertions.assertNotNull(releaseListResult);
        Assertions.assertEquals(releaseListResult.size(), 2);
    }

    @Test
    void searchReleaseByIdWithSuccess(){
        Optional<Release> release = Optional.of(createDefaultRelease());
        release.get().setId(1L);

        when(releaseRepository.findById(anyLong())).thenReturn(release);

        Optional<Release> releaseResult = releaseService.searchReleaseById(1L);

        Assertions.assertTrue(releaseResult.isPresent());
    }

    @Test
    void searchReleaseByIdEmpty(){
        when(releaseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Release> releaseResult = releaseService.searchReleaseById(1L);

        Assertions.assertFalse(releaseResult.isPresent());
    }

    @Test
    void validateErrors(){
        Release release = new Release();

        Throwable erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_DESCRIPTION);

        release.setDescription("salario");

        erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_MONTH);

        release.setMonth(1);

        erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_YEAR);

        release.setYear(2022);

        erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_USER);

        release.setUser(new User());

        erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_VALUE);

        release.setValue(BigDecimal.valueOf(100));

        erro = Assertions.assertThrows(BusinessRuleException.class, () -> releaseService.validate(release));
        Assertions.assertEquals(erro.getMessage(), Constants.INVALID_RELEASE_TYPE);
    }

    private Release createDefaultRelease(){
        return Release.builder()
                .user(User.builder()
                        .name("Teste")
                        .email("teste@email.com")
                        .password("123")
                        .build())
                .description("descricao de teste")
                .month(1)
                .year(2022)
                .value(BigDecimal.valueOf(10))
                .releaseType(ReleaseType.RECEITA)
                .build();
    }

}
