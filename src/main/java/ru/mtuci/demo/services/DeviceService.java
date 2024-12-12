package ru.mtuci.demo.services;

import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.User;

public interface DeviceService {
    public Device registerOrUpdateDevice(String deviceInfo, User user, String Name);
}
