package com.github.mpacala00.forum.service.dto;

import com.github.mpacala00.forum.model.User;
import com.github.mpacala00.forum.model.dto.UserDTO;

public class UserDTOMappingService implements DTOMappingService<User, UserDTO> {

    @Override
    public UserDTO convertToDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setId(entity.getId());
        dto.setRole(entity.getRole());
        return dto;
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());
        user.setRole(userDTO.getRole());
        return user;
    }
}
