    public static void delete(String name) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "delete from de.anhquan.demo.hibernate.helloworld.Order where name = :name";
            Query query = session.createQuery(hql);
            query.setParameter("name", name);
            int row = query.executeUpdate();
            if (row == 0) {
                System.out.println("Doesn't deleted any row!");
            } else {
                System.out.println("Deleted Row: " + row);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                try {
                    tx.rollback();
                } catch (HibernateException e1) {
                    System.out.println("Error rolling back transaction");
                }
                throw e;
            }
        }
    }
