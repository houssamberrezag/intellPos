package com.intell.pos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.intell.pos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseCategorieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseCategorie.class);
        ExpenseCategorie expenseCategorie1 = new ExpenseCategorie();
        expenseCategorie1.setId(1L);
        ExpenseCategorie expenseCategorie2 = new ExpenseCategorie();
        expenseCategorie2.setId(expenseCategorie1.getId());
        assertThat(expenseCategorie1).isEqualTo(expenseCategorie2);
        expenseCategorie2.setId(2L);
        assertThat(expenseCategorie1).isNotEqualTo(expenseCategorie2);
        expenseCategorie1.setId(null);
        assertThat(expenseCategorie1).isNotEqualTo(expenseCategorie2);
    }
}
