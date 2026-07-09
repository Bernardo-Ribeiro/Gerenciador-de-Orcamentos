package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

class UsuarioTest {

    @Test
    void testCriarUsuario() {
        Usuario usuario = new Usuario();
        assertThat(usuario).isNotNull();
    }

    @Test
    void testSetters() {
        Usuario usuario = new Usuario();
        
        usuario.setId(1);
        usuario.setNome("Administrador");
        usuario.setEmail("admin@grafica.com");
        usuario.setSenhaHash("hash_senha_segura");
        usuario.setStatus("ATIVO");
        
        assertThat(usuario.getId()).isEqualTo(1);
        assertThat(usuario.getNome()).isEqualTo("Administrador");
        assertThat(usuario.getEmail()).isEqualTo("admin@grafica.com");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash_senha_segura");
        assertThat(usuario.getStatus()).isEqualTo("ATIVO");
    }

    @Test
    void testCriarUsuarioComConstrutorSimples() {
        Usuario usuario = new Usuario(
            "Usuario Teste",
            "teste@grafica.com",
            "hash123",
            "ATIVO"
        );
        
        assertThat(usuario.getNome()).isEqualTo("Usuario Teste");
        assertThat(usuario.getEmail()).isEqualTo("teste@grafica.com");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash123");
        assertThat(usuario.getStatus()).isEqualTo("ATIVO");
        assertThat(usuario.getDataCadastro()).isNotNull();
    }

    @Test
    void testCriarUsuarioCompleto() {
        LocalDateTime dataCadastro = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        Usuario usuario = new Usuario(
            1,
            "Admin",
            "admin@grafica.com",
            "hash456",
            "ATIVO",
            dataCadastro
        );
        
        assertThat(usuario.getId()).isEqualTo(1);
        assertThat(usuario.getNome()).isEqualTo("Admin");
        assertThat(usuario.getEmail()).isEqualTo("admin@grafica.com");
        assertThat(usuario.getSenhaHash()).isEqualTo("hash456");
        assertThat(usuario.getStatus()).isEqualTo("ATIVO");
        assertThat(usuario.getDataCadastro()).isEqualTo(dataCadastro);
    }

    @Test
    void testToString() {
        Usuario usuario = new Usuario("Teste", "teste@test.com", "hash", "ATIVO");
        String toString = usuario.toString();
        
        assertThat(toString).contains("nome='Teste'");
        assertThat(toString).contains("email='teste@test.com'");
    }
}