    @SuppressWarnings("unchecked")
    protected static void executePostHibernateStartUpSQL(final ServletConfig config, final Lifecycle lc) {
        Session s = lc.getHibernateDatasourceDirectly().createNewSession();
        Transaction tx = null;
        Level oldLevel = Logger.getLogger("org.hibernate.util.JDBCExceptionReporter").getLevel();
        try {
            Logger.getLogger("org.hibernate.util.JDBCExceptionReporter").setLevel(Level.OFF);
            for (Element statement : (List<Element>) config.getPostHibernateStartUpSQL().getChildren("statement")) {
                boolean ignoreErrors = false;
                String sql = statement.getText().trim();
                if (statement.getAttributeValue("ignoreErrors").equalsIgnoreCase("true")) {
                    ignoreErrors = true;
                }
                try {
                    tx = s.beginTransaction();
                    s.createSQLQuery(sql).executeUpdate();
                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    if (!ignoreErrors) {
                        LOGGER.warn(sql + System.getProperty("line.separator") + e.getLocalizedMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
            Logger.getLogger("org.hibernate.util.JDBCExceptionReporter").setLevel(oldLevel);
        }
    }
