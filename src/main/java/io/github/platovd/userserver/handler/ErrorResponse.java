package io.github.platovd.userserver.handler;

import java.util.HashMap;

public record ErrorResponse(
        HashMap<String, String> errors
) {
}
