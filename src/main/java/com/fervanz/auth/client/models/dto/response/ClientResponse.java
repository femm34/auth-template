package com.fervanz.auth.client.models.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientResponse {
    private String username;
    private String email;
    private String name;
    private String motherSurname;
    private String fatherSurname;
}
