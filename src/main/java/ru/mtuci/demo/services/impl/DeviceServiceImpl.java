package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.DeviceRepository;
import ru.mtuci.demo.services.DeviceService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    public Device getDeviceByDeviceInfo(String deviceInfo) {
        return deviceRepository.findByMac(deviceInfo)
                .orElseThrow(() -> new DeviceNotFoundException("Для данного Устройства не найдена лицензия"));
    }
        public Device registerOrUpdateDevice(String deviceInfo, User user,String Name) {

        Device device = deviceRepository.findByMac(deviceInfo)
                .orElse(new Device());
        device.setMac(deviceInfo);
        device.setUser(user);
        device.setName(Name);
        return deviceRepository.save(device);
    }
}

