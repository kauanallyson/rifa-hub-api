package com.kauanallyson.rifa_hub_api.dto.vendedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VendedorCreateDTO(
    @NotBlank(message = "O nome do vendedor é obrigatório")
    String nome,

    @NotBlank(message = "O telefone do vendedor é obrigatório")
    String telefone,

    @Email(message = "O formato do e-mail é inválido")
    String email
) {}
