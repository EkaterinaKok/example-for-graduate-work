package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;

@Service
public interface UsersService {

    void setPassword(NewPassword dto);
    ru.skypro.homework.dto.User getUser();
    void updateUser(UpdateUser dto);
    void updateUserImage(MultipartFile image);

}
