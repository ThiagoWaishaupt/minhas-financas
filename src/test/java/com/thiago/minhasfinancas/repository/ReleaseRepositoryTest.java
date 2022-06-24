package com.thiago.minhasfinancas.repository;

import com.thiago.minhasfinancas.model.enums.ReleaseType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ReleaseRepositoryTest {

    @Mock
    ReleaseRepository releaseRepository;

    @Test
    void getBalance(){
        when(releaseRepository.getUserBalance(1L, ReleaseType.RECEITA)).thenReturn(BigDecimal.valueOf(100));

        BigDecimal result = releaseRepository.getUserBalance(1L, ReleaseType.RECEITA);

        Assertions.assertEquals(result, BigDecimal.valueOf(100));
    }


}
