    private static void createPersonProcessStateTable() {
        try {
            sf.getCurrentSession().beginTransaction();
            sf.getCurrentSession().createSQLQuery(createTableSql).executeUpdate();
            sf.getCurrentSession().getTransaction().commit();
        } catch (HibernateException e) {
            if (e.getCause() instanceof SQLException && ((SQLException) e.getCause()).getSQLState().equals("X0Y32")) {
                System.out.println("Table already exists in database");
                sf.getCurrentSession().getTransaction().rollback();
            }
        }
    }
