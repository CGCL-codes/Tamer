    public int updatePermissionsForUserAndTypeAndAgenda(Integer userId, Integer eventTypeId, Integer agendaId, String permissions) throws TechnicalException {
        if (permissions == null) {
            throw new TechnicalException(new Exception(new Exception("Column 'permissions' cannot be null")));
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getCurrentSession();
            transaction = session.beginTransaction();
            String query = "UPDATE j_user_type_agenda SET permissions=\"" + permissions + "\" where userId=" + userId + " AND eventTypeId=" + eventTypeId + " AND agendaId=" + agendaId;
            Statement statement = session.connection().createStatement();
            int rowsUpdated = statement.executeUpdate(query);
            transaction.commit();
            return rowsUpdated;
        } catch (HibernateException ex) {
            if (transaction != null) transaction.rollback();
            throw new TechnicalException(ex);
        } catch (SQLException e) {
            if (transaction != null) transaction.rollback();
            throw new TechnicalException(e);
        }
    }
