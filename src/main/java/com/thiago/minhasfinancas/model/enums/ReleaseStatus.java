package com.thiago.minhasfinancas.model.enums;

import com.thiago.minhasfinancas.exception.BusinessRuleException;

public enum ReleaseStatus {

    PENDENTE,
    CANCELADO,
    EFETIVADO;

    public static ReleaseStatus getReleaseStatus(String status){
        try {
            return ReleaseStatus.valueOf(status);
        } catch (IllegalArgumentException e){
            return null;
        }

    }
}
