package com.kauanallyson.rifa_hub_api.dtos.vendedor;

import jakarta.validation.constraints.Email;

public record VendedorUpdateDTO(
        String nome,
        String telefone,
        @Email String email
) {
}
