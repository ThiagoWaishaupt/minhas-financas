package com.thiago.minhasfinancas.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReleaseDTO {

    private Long id;
    private String description;
    private Integer month;
    private Integer year;
    private BigDecimal value;
    private Long userId;
    private String type;
    private String status;

}
