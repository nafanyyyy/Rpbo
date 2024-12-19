package ru.mtuci.demo.services;

import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.User;

import java.util.Optional;

public interface DeviceService {
    Device getDeviceByDeviceInfo(String deviceInfo);
    Device registerOrUpdateDevice(String deviceInfo, User user, String Name);
}
