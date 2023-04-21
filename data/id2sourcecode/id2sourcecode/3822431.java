    public int deletePermissionsForUserAndTypeAndAgenda(Integer userId, Integer eventTypeId, Integer agendaId) throws TechnicalException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getCurrentSession();
            transaction = session.beginTransaction();
            String query = "DELETE FROM j_user_type_agenda where userId=" + userId + " AND eventTypeId=" + eventTypeId + " AND agendaId=" + agendaId;
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
