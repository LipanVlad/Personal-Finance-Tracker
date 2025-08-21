package finance.code;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class QueryUtil {
    Session session = null;

    public Double getLastBalance() {
        Double balance = 0.0;

        try {
            initDbQuery();
            Query<Double> query = session.createQuery("SELECT balance FROM UserStatus ORDER BY id DESC", Double.class);
            query.setMaxResults(1);
            balance = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if (balance != null) {
            return balance;
        } else {
            return 0.0;
        }
    }

    public Double getLastBudgetCap() {
        Double balance = 0.0;

        try {
            initDbQuery();
            Query<Double> query = session.createQuery("SELECT budgetCap FROM UserStatus ORDER BY id DESC", Double.class);
            query.setMaxResults(1);
            balance = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        if (balance != null) {
            return balance;
        } else {
            return 0.0;
        }

    }

    public Double getLastSum() {
        Double lastSum = 0.0;

        try {
            initDbQuery();
            Query<Double> query = session.createQuery("SELECT expenseSum FROM UserStatus ORDER BY id DESC", Double.class);
            query.setMaxResults(1);
            lastSum = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        
        if (lastSum != null) {
            return lastSum;
        } else {
            return 0.0;
        }
    }

    private void initDbQuery() {
        Configuration config = new Configuration();
        config.addAnnotatedClass(UserStatus.class);
        config.configure();

        SessionFactory factory = config.buildSessionFactory();
        this.session = factory.openSession();
    }
}
