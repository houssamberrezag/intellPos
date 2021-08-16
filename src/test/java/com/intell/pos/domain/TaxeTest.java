package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Taxe.class);
        Taxe taxe1 = new Taxe();
        taxe1.setId(1L);
        Taxe taxe2 = new Taxe();
        taxe2.setId(taxe1.getId());
        assertThat(taxe1).isEqualTo(taxe2);
        taxe2.setId(2L);
        assertThat(taxe1).isNotEqualTo(taxe2);
        taxe1.setId(null);
        assertThat(taxe1).isNotEqualTo(taxe2);
    }
}
