package com.moditraders.services;

import com.moditraders.entities.ExpensesEntity;
import com.moditraders.models.Expense;
import com.moditraders.models.UserModel;
import com.moditraders.repositories.ExpenseRepository;
import com.moditraders.types.ExpenseType;
import com.moditraders.utility.ConversionUtility;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Hitesh Modi on 02-03-2018.
 */
@Component("expenseService")
public class ExpenseService implements IExpenseService {

    @Resource(name = "expenseRepository")
    private ExpenseRepository expenseRepo;

    @Resource(name = "userService")
    private IUserService userService;

    @Override
    public Expense getExpense(long expenseId) {
        ExpensesEntity expensesEntity = expenseRepo.findOne(expenseId);
        return ConversionUtility.convertExpenseEntityToModel(expensesEntity);
    }

    @Override
    public Long saveExpense(Expense expense) {
        UserModel user = userService.getUserInfo();
        ExpensesEntity entity = ConversionUtility.convertExpenseModelToEntity(expense);
        entity.setUserid(user.getUserid());
        ExpensesEntity savedEntity = expenseRepo.save(entity);
        return savedEntity.getExpenseid();
    }

    @Override
    public ExpenseType[] getExpenseTypes() {
        return ExpenseType.values();
    }
}
