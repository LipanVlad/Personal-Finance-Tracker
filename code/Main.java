package finance.code;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);

        int choice = 0, newValue = 0, id = 0;

        Configuration config = new Configuration();

        config.addAnnotatedClass(Expense.class);
        config.addAnnotatedClass(Income.class);
        config.addAnnotatedClass(UserStatus.class);
        config.configure();

        SessionFactory factory = config.buildSessionFactory();

        while (choice != 6) {
            System.out.println("\n1. Add income");
            System.out.println("2. Add expense");
            System.out.println("3. Edit income value based on ID");
            System.out.println("4. Edit expense value based on ID");
            System.out.println("5. Set budget limit");
            System.out.println("6. Exit");
            System.out.print("Input a number: ");
            choice = cin.nextInt();
            cin.nextLine();


            switch (choice) {
                case 1:
                    addFinancialRecord(factory, "income");
                    break;
                case 2:
                    addFinancialRecord(factory, "expense");
                    break;
                case 3:
                    System.out.print("Enter the id: ");
                    id = cin.nextInt();
                    System.out.print("Enter new value: ");
                    newValue = cin.nextInt();
                    editFinancialRecord(factory, "income", newValue, id);
                    break;
                case 4:
                    System.out.print("Enter the id: ");
                    id = cin.nextInt();
                    System.out.print("Enter new value: ");
                    newValue = cin.nextInt();
                    editFinancialRecord(factory, "expense", newValue, id);
                    break;
                case 5:
                    System.out.print("New budget limit: ");
                    Double budget = cin.nextDouble();
                    setBudgetCap(factory, budget);
                    System.out.print("Budget limit set!");


                    break;
                case 6:
                    System.out.println("Exit confirmed");
                    break;
                default:
                    System.out.println("Invalid choice, exiting program");
                    choice = 6;
                    break;
            }

        }

        factory.close();
    }

    private static void addFinancialRecord(SessionFactory factory, String recordType) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            UserStatus userStatus = new UserStatus();
            QueryUtil queryUtil = new QueryUtil();
            int value = Toolbox.intInput();
            String category = Toolbox.stringCatInput();
            String description = Toolbox.stringDescInput();

            if ("income".equals(recordType)) {
                Income income = new Income();
                income.setValue(value);
                income.setCategory(category);
                income.setDescription(description);
                session.persist(income);


                Double balance = queryUtil.getLastBalance();
                userStatus.setBalance(balance + income.getValue());
                userStatus.setBudgetCap(queryUtil.getLastBudgetCap());
                session.persist(userStatus);

                System.out.println("\nIncome added successfully!");
            } else if ("expense".equals(recordType)) {
                Expense expense = new Expense();
                expense.setValue(value);
                expense.setCategory(category);
                expense.setDescription(description);

                session.persist(expense);

                Double balance = queryUtil.getLastBalance();
                userStatus.setBalance(balance - expense.getValue());

                Double lastExpenseSum = queryUtil.getLastSum();
                userStatus.setExpenseSum(lastExpenseSum + expense.getValue());
                userStatus.setBudgetCap(queryUtil.getLastBudgetCap());

                if (userStatus.getBudgetCap() != null &&
                        userStatus.getExpenseSum() > userStatus.getBudgetCap()) {
                    System.out.println("WARNING, EXCEEDING SET BUDGET!!!");
                }

                session.persist(userStatus);


                System.out.println("\nExpense added successfully!");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void editFinancialRecord(SessionFactory factory, String recordType, int newValue, int id) {
        Session session = null;
        Transaction transaction = null;
        QueryUtil queryUtil = new QueryUtil();
        UserStatus userStatus = new UserStatus();
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            if ("income".equals(recordType)) {
                Income income = session.byId(Income.class).load(id);

                if (newValue > income.getValue()) {
                    userStatus.setBalance(queryUtil.getLastBalance() + (newValue - income.getValue()));
                } else {
                    userStatus.setBalance(queryUtil.getLastBalance() - (income.getValue() - newValue));
                }


                income.setValue(newValue);
                userStatus.setBudgetCap(queryUtil.getLastBudgetCap());
                userStatus.setExpenseSum(queryUtil.getLastSum());
                session.persist(userStatus);
                session.persist(income);

                System.out.println("New income value set!");
            } else if ("expense".equals(recordType)) {
                Expense expense = session.byId(Expense.class).load(id);

                if (newValue > expense.getValue()) {
                    userStatus.setExpenseSum(queryUtil.getLastSum() + (newValue - expense.getValue()));
                    userStatus.setBalance(queryUtil.getLastBalance() - (newValue - expense.getValue()));
                } else {
                    userStatus.setExpenseSum(queryUtil.getLastSum() - (expense.getValue() - newValue));
                    userStatus.setBalance(queryUtil.getLastBalance() + (expense.getValue() - newValue));
                }

                expense.setValue(newValue);
                userStatus.setBudgetCap(queryUtil.getLastBudgetCap());
                session.persist(userStatus);

                if (userStatus.getBudgetCap() != null &&
                        userStatus.getExpenseSum() > userStatus.getBudgetCap()) {
                    System.out.println("WARNING, EXCEEDING SET BUDGET!!!");
                }

                session.persist(expense);

                System.out.println("New expense value set!");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void setBudgetCap(SessionFactory factory, Double budget) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();

            UserStatus userStatus = new UserStatus();
            QueryUtil queryUtil = new QueryUtil();

            userStatus.setBudgetCap(budget);

            userStatus.setExpenseSum(0.0);

            Double balance = queryUtil.getLastBalance();
            userStatus.setBalance(balance);


            session.persist(userStatus);

            System.out.println("\nExpense added successfully!");
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}