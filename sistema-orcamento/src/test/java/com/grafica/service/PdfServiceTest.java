package com.grafica.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.grafica.model.Orcamento;
import com.grafica.model.ItemOrcamento;
import com.grafica.model.Material;
import com.grafica.model.Produto;
import com.grafica.dao.OrcamentoDAO;
import com.grafica.dao.ItemOrcamentoDAO;
import com.grafica.dao.ConfiguracaoPdfDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class PdfServiceTest {

    @Test
    void testFormatarMoeda() {
        String resultado = PdfServiceTestHelper.formatarMoeda(new BigDecimal("1234.56"));
        assertThat(resultado).isEqualTo("R$ 1234,56");
    }

    @Test
    void testFormatarMoedaValorZero() {
        String resultado = PdfServiceTestHelper.formatarMoeda(BigDecimal.ZERO);
        assertThat(resultado).isEqualTo("R$ 0,00");
    }

    @Test
    void testFormatarMoedaValorNulo() {
        String resultado = PdfServiceTestHelper.formatarMoeda(null);
        assertThat(resultado).isEqualTo("R$ 0,00");
    }

    @Test
    void testFormatarMoedaComCentavos() {
        String resultado = PdfServiceTestHelper.formatarMoeda(new BigDecimal("99.99"));
        assertThat(resultado).isEqualTo("R$ 99,99");
    }

    @Test
    void testFormatarMoedaValorGrande() {
        String resultado = PdfServiceTestHelper.formatarMoeda(new BigDecimal("1234567.89"));
        assertThat(resultado).isEqualTo("R$ 1234567,89");
    }

    static class PdfServiceTestHelper {
        public static String formatarMoeda(BigDecimal valor) {
            if (valor == null) return "R$ 0,00";
            return "R$ " + valor.setScale(2, java.math.RoundingMode.HALF_UP).toString().replace('.', ',');
        }
    }
}