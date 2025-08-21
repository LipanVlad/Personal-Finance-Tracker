package finance.code;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserStatus {

    Double balance = 0.0;
    Double budgetCap;
    Double expenseSum = 0.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Double getExpenseSum() {
        return expenseSum;
    }

    public void setExpenseSum(Double expenseSum) {
        this.expenseSum = expenseSum;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBudgetCap() {
        return budgetCap;
    }

    public void setBudgetCap(Double budgetCap) {
        this.budgetCap = budgetCap;
    }
}
