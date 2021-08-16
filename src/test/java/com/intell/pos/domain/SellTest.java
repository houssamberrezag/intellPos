package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SellTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sell.class);
        Sell sell1 = new Sell();
        sell1.setId(1L);
        Sell sell2 = new Sell();
        sell2.setId(sell1.getId());
        assertThat(sell1).isEqualTo(sell2);
        sell2.setId(2L);
        assertThat(sell1).isNotEqualTo(sell2);
        sell1.setId(null);
        assertThat(sell1).isNotEqualTo(sell2);
    }
}
