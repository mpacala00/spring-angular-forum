package com.github.mpacala00.forum.service.dto;

public interface DTOMappingService<T, DTO> {

    DTO convertToDTO(T entity);
    T convertToEntity(DTO dto);
}
