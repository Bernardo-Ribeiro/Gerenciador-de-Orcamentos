package com.grafica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

class EscalaProdutivaTest {

    @Test
    void testCriarEscalaProdutiva() {
        EscalaProdutiva escala = new EscalaProdutiva();
        
        assertThat(escala).isNotNull();
    }

    @Test
    void testSetters() {
        EscalaProdutiva escala = new EscalaProdutiva();
        
        escala.setId(1);
        escala.setIdMaterial(10);
        escala.setQtdMinima(100);
        escala.setQtdMaxima(500);
        escala.setDescontoPercentual(new BigDecimal("15.00"));
        
        assertThat(escala.getId()).isEqualTo(1);
        assertThat(escala.getIdMaterial()).isEqualTo(10);
        assertThat(escala.getQtdMinima()).isEqualTo(100);
        assertThat(escala.getQtdMaxima()).isEqualTo(500);
        assertThat(escala.getDescontoPercentual()).isEqualByComparingTo(new BigDecimal("15.00"));
    }

    @Test
    void testDescontoProgressivo() {
        EscalaProdutiva escala1 = new EscalaProdutiva(1, 10, 1, 50, new BigDecimal("0.00"));
        EscalaProdutiva escala2 = new EscalaProdutiva(2, 10, 51, 500, new BigDecimal("10.00"));
        EscalaProdutiva escala3 = new EscalaProdutiva(3, 10, 501, 1000, new BigDecimal("15.00"));
        
        assertThat(escala1.getDescontoPercentual()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(escala2.getDescontoPercentual()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(escala3.getDescontoPercentual()).isEqualByComparingTo(new BigDecimal("15.00"));
    }
}