    protected synchronized void uptadeQuery(String uptade, String conditionQueryString, String nameClass) throws HelpDeskException {
        String queryCompleta = "update " + nameClass + " set " + uptade + " where " + conditionQueryString;
        Session session = openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(queryCompleta);
            int valor = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new HelpDeskException(MsgErros.OPER_NAO_REALIZADA.msg("Atualiza��o", e.getMessage()));
        } finally {
            session.flush();
            session.close();
        }
    }
