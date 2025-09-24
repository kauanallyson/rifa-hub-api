package com.kauanallyson.rifa_hub_api.dtos.comprador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompradorCreateDTO(
        @NotBlank(message = "O nome do comprador é obrigatório")
        String nome,

        @NotBlank(message = "O telefone do comprador é obrigatório")
        String telefone,

        @Email(message = "O formato do e-mail é inválido")
        String email
) {
}
