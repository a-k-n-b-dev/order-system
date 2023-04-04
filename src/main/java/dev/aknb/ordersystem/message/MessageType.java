package dev.aknb.ordersystem.message;

public enum MessageType {

    ERROR,
    EMAIL_EXISTS,
    PHONE_NUMBER_EXISTS,
    EMAIL_VERIFICATION,
    EMAIL_NOT_VERIFIED,
    ONCE_PER_MINUTE,
    VERIFICATION_TOKEN_NOT_FOUND,
    ORDER_NOT_FOUND_BY_ID,
    VERIFICATION_TOKEN_EXPIRED,
    INVALID_CREDENTIALS,
    EMPTY_CREDENTIALS,
    USER_NOT_FOUND_EMAIL,
    ALREADY_VERIFIED,
    RESET_PASSWORD,
    PASSWORD_NOT_MATCHED,
    APPROVE
}
