package com.fervanz.auth.shared.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalResponse {
    private String message;
    private Object data;
    private Object status;
    private int code;
}
