package com.kauanallyson.rifa_hub_api.dtos.comprador;

import jakarta.validation.constraints.Email;

public record CompradorUpdateDTO(
        String nome,
        String telefone,
        @Email String email
) {
}
