package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReturnTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReturnTransaction.class);
        ReturnTransaction returnTransaction1 = new ReturnTransaction();
        returnTransaction1.setId(1L);
        ReturnTransaction returnTransaction2 = new ReturnTransaction();
        returnTransaction2.setId(returnTransaction1.getId());
        assertThat(returnTransaction1).isEqualTo(returnTransaction2);
        returnTransaction2.setId(2L);
        assertThat(returnTransaction1).isNotEqualTo(returnTransaction2);
        returnTransaction1.setId(null);
        assertThat(returnTransaction1).isNotEqualTo(returnTransaction2);
    }
}
