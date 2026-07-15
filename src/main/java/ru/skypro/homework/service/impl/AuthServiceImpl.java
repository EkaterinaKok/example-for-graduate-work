package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UsersRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String username, String password) {
        return usersRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .isPresent();
    }

    @Override
    public boolean register(Register register) {
        if (usersRepository.findByUsername(register.getUsername()).isPresent()) {
            return false;
        }

        UserEntity user = new UserEntity();
        user.setUsername(register.getUsername());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setPhone(register.getPhone());
        user.setRole(register.getRole() != null ? register.getRole().name() : "USER");

        user.setPasswordHash(passwordEncoder.encode(register.getPassword()));

        usersRepository.save(user);
        return true;
    }

}
