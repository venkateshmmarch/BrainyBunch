package com.example.loanService.entity;

import lombok.Getter;
import lombok.Setter;

@Getter

public enum BookStatus {
    AVAILABLE,
    NOT_AVAILABLE,
    LOANED;
}
