package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubcategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subcategorie.class);
        Subcategorie subcategorie1 = new Subcategorie();
        subcategorie1.setId(1L);
        Subcategorie subcategorie2 = new Subcategorie();
        subcategorie2.setId(subcategorie1.getId());
        assertThat(subcategorie1).isEqualTo(subcategorie2);
        subcategorie2.setId(2L);
        assertThat(subcategorie1).isNotEqualTo(subcategorie2);
        subcategorie1.setId(null);
        assertThat(subcategorie1).isNotEqualTo(subcategorie2);
    }
}
