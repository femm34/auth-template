package com.fervanz.auth.client.models.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ClientRequest {
    private String username;
    private String email;
    private String name;
    private String motherSurname;
    private String fatherSurname;
}
