package com.kauanallyson.rifa_hub_api.exceptions.handler;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
