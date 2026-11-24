package org.dt2.mercado;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI documentacaoApi() {
        return new OpenAPI().info(new Info()
                .title("API Mercado")
                .version("1.0.0")
                .description("API REST para gerenciar usuários, categorias e produtos."));
    }
}
