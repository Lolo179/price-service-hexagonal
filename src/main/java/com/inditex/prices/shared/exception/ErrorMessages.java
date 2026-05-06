package com.inditex.prices.shared.exception;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String PRICE_NOT_FOUND =
            "No applicable price found for brandId=%d, productId=%d at %s";

    public static final String MISSING_PARAMETER =
            "Missing required parameter: %s";

    public static final String INVALID_PARAMETER =
            "Invalid value '%s' for parameter '%s'";

    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected error occurred. Please try again later.";
}
