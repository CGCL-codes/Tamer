    public static synchronized Integer ExecuteSQL(String SQL, Connection conexion) {
        Transaction tx = null;
        try {
            Session session = InitSessionFactory.getSessionFactory().getCurrentSession();
            tx = session.beginTransaction();
            Integer affected = new Integer(session.createSQLQuery(SQL).executeUpdate());
            tx.commit();
            return affected;
        } catch (HibernateException he) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new HibernateException("No se pudo ejecutar la instrucción contra la base de datos: " + tx.toString());
        }
    }
