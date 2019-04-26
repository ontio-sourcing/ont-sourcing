package com.ontology.sourcing.util.exp;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ONTSourcingException extends Exception {

    ErrorCode errorCode;

    public ONTSourcingException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
