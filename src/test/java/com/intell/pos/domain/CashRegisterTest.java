package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CashRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashRegister.class);
        CashRegister cashRegister1 = new CashRegister();
        cashRegister1.setId(1L);
        CashRegister cashRegister2 = new CashRegister();
        cashRegister2.setId(cashRegister1.getId());
        assertThat(cashRegister1).isEqualTo(cashRegister2);
        cashRegister2.setId(2L);
        assertThat(cashRegister1).isNotEqualTo(cashRegister2);
        cashRegister1.setId(null);
        assertThat(cashRegister1).isNotEqualTo(cashRegister2);
    }
}
