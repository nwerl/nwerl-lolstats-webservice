package com.nwerl.lolstats.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
//400 	Bad request
//401 	Unauthorized
//403 	Forbidden
//404 	Data not found
//405 	Method not allowed
//415 	Unsupported media type
//429 	Rate limit exceeded
//500 	Internal server error
//502 	Bad gateway
//503 	Service unavailable
//504 	Gateway timeout

    API_UNAUTHORIZED(401, "R001", "Check API Key"),
    API_FORBIDDEN(403, "R002", "API Forbidden"),
    API_DATA_NOT_FOUND(404, "R003", "API Data Not Found"),
    API_METHOD_NOT_ALLOWED(405, "R004", "API Method Not Allowed"),
    API_TOO_MANY_REQUEST(429, "R005", "API Too Many Request"),
    API_SERVICE_UNAVAILABLE(503, "R006", "API Server Unavailable"),
    API_GATEWAY_TIMEOUT(504, "R007", "API Gateway Timeout"),
    INTERNAL_SERVER_ERROR(500, "C004", "Internal Server error")
    ;

    private Integer status;
    private final String code;
    private final String message;

    ErrorCode(final Integer status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
