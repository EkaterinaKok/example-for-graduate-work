package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserEntityMapper;
import ru.skypro.homework.repository.UserEntityRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityRepository userEntityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserEntityMapper userEntityMapper;

    @Transactional(readOnly = true)
    @Override
    public boolean login(String userName, String password) {
        return userEntityRepository.findByUsername(userName)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Transactional
    @Override
    public boolean register(Register register) {
        if (userEntityRepository.findByUsername(register.getUsername()).isPresent()) {
            return false;
        }

        UserEntity userEntity = userEntityMapper.registerUser(register);
        userEntity.setPassword(passwordEncoder.encode(register.getPassword()));
        userEntityRepository.save(userEntity);

        return true;
    }

}
