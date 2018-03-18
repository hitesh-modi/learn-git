package com.moditraders.services;

import com.moditraders.models.Expense;
import com.moditraders.types.ExpenseType;

/**
 * Created by Hitesh Modi on 02-03-2018.
 */
public interface IExpenseService {
    public Expense getExpense(long expenseId);
    public Long saveExpense(Expense expense);
    public ExpenseType[] getExpenseTypes();
}
