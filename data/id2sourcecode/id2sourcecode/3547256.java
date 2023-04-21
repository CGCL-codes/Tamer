    public static long executeUpdate(String sql, List<ISParameter> qualifications, boolean iKnowWhatIAmDoing) throws Exception {
        if (iKnowWhatIAmDoing) {
            Session session = HibernateUtil.getNewSession();
            session.beginTransaction();
            try {
                SQLQuery query = session.createSQLQuery(sql);
                addSQLParameters(query, qualifications);
                int result = query.executeUpdate();
                return result;
            } catch (Exception ex) {
                Log.exception(ex);
                session.getTransaction().rollback();
                throw ex;
            } finally {
                session.close();
            }
        } else {
            return 0L;
        }
    }
