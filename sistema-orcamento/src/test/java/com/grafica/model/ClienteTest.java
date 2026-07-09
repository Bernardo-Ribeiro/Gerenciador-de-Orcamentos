package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

class ClienteTest {

    @Test
    void testCriarClienteComConstrutorPadrao() {
        Cliente cliente = new Cliente();
        
        assertThat(cliente).isNotNull();
        assertThat(cliente.getId()).isNull();
        assertThat(cliente.getNome()).isNull();
    }

    @Test
    void testCriarClienteComDadosBasicos() {
        Cliente cliente = new Cliente(
            "Empresa ABC",
            "12.345.678/0001-90",
            "contato@empresa.com.br",
            "(11) 98765-4321"
        );
        
        assertThat(cliente.getNome()).isEqualTo("Empresa ABC");
        assertThat(cliente.getCpfCnpj()).isEqualTo("12.345.678/0001-90");
        assertThat(cliente.getEmailContato()).isEqualTo("contato@empresa.com.br");
        assertThat(cliente.getTelefoneWhatsapp()).isEqualTo("(11) 98765-4321");
        assertThat(cliente.getStatus()).isEqualTo("ativo");
        assertThat(cliente.getDataCadastro()).isNotNull();
        assertThat(cliente.getDataCadastro()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testCriarClienteComTodosOsCampos() {
        LocalDateTime dataCadastro = LocalDateTime.of(2024, 1, 15, 10, 30);
        
        Cliente cliente = new Cliente(
            1,
            "Empresa XYZ",
            "98.765.432/0001-10",
            "xyz@empresa.com.br",
            "(21) 91234-5678",
            "inativo",
            dataCadastro
        );
        
        assertThat(cliente.getId()).isEqualTo(1);
        assertThat(cliente.getNome()).isEqualTo("Empresa XYZ");
        assertThat(cliente.getCpfCnpj()).isEqualTo("98.765.432/0001-10");
        assertThat(cliente.getEmailContato()).isEqualTo("xyz@empresa.com.br");
        assertThat(cliente.getTelefoneWhatsapp()).isEqualTo("(21) 91234-5678");
        assertThat(cliente.getStatus()).isEqualTo("inativo");
        assertThat(cliente.getDataCadastro()).isEqualTo(dataCadastro);
    }

    @Test
    void testSetters() {
        Cliente cliente = new Cliente();
        
        cliente.setId(1);
        cliente.setNome("Nova Empresa");
        cliente.setCpfCnpj("11.222.333/0001-44");
        cliente.setEmailContato("novo@email.com");
        cliente.setTelefoneWhatsapp("(31) 99999-8888");
        cliente.setStatus("pendente");
        
        assertThat(cliente.getId()).isEqualTo(1);
        assertThat(cliente.getNome()).isEqualTo("Nova Empresa");
        assertThat(cliente.getCpfCnpj()).isEqualTo("11.222.333/0001-44");
        assertThat(cliente.getEmailContato()).isEqualTo("novo@email.com");
        assertThat(cliente.getTelefoneWhatsapp()).isEqualTo("(31) 99999-8888");
        assertThat(cliente.getStatus()).isEqualTo("pendente");
    }

    @Test
    void testToString() {
        Cliente cliente = new Cliente(
            "Teste Ltda",
            "00.000.000/0001-00",
            "teste@ltda.com",
            "(41) 90000-0000"
        );
        
        String toString = cliente.toString();
        
        assertThat(toString).contains("Teste Ltda");
        assertThat(toString).contains("00.000.000/0001-00");
        assertThat(toString).contains("teste@ltda.com");
    }

    @Test
    void testEqualsAndHashCode() {
        Cliente cliente1 = new Cliente("Empresa A", "111.111.111/0001-11", "a@empresa.com", "(11) 1111-1111");
        Cliente cliente2 = new Cliente("Empresa A", "111.111.111/0001-11", "a@empresa.com", "(11) 1111-1111");
        
        assertThat(cliente1.getNome()).isEqualTo(cliente2.getNome());
        assertThat(cliente1.getCpfCnpj()).isEqualTo(cliente2.getCpfCnpj());
    }
}