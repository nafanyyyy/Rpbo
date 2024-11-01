package ru.mtuci.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    READ("read"),
    MODIFICATION("modification");

    private final String permission;

    @Override
    public String toString() {
        return permission;
    }
}