package ru.mtuci.demo.services;

import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;

import java.util.List;

public interface LicenseService {
    void add(License license);

    List<License> getAll();
}
