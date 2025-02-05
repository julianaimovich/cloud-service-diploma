package ru.netology.cloudservice.util;

import org.mockito.stubbing.Answer;
import ru.netology.cloudservice.model.BaseEntity;

public class MockUtil {

    public static <T> Answer<T> saveWithId(Long id) {
        return invocation -> {
            T entity = invocation.getArgument(0);
            if (entity instanceof BaseEntity) {
                ((BaseEntity) entity).setId(id);
            }
            return entity;
        };
    }
}