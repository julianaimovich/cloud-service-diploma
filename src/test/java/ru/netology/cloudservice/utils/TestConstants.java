package ru.netology.cloudservice.utils;

public interface TestConstants {

    interface RoleAuthorities {
        String ROLE_ADMIN_AUTHORITY = "ROLE_ADMIN";
        String ROLE_USER_AUTHORITY = "ROLE_USER";
    }

    interface ExceptionMessages {
        String NOT_NULL_PROPERTY_NULL_REFERENCE = "not-null property references a null or transient value";
    }
}