    protected synchronized void removeAll(String deleteFrom) throws HelpDeskException {
        Session session = openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query queryDelete = session.createQuery("delete from " + deleteFrom);
            queryDelete.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new HelpDeskException(MsgErros.OPER_NAO_REALIZADA.msg("Limpeza de cadastro", e.getMessage()));
        } finally {
            session.flush();
            session.close();
        }
    }
