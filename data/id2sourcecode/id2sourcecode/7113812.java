    public void delete(long id) {
        Session sessao = getSession().getCurrentSession();
        Transaction transacao = sessao.beginTransaction();
        try {
            sessao.createQuery("delete Reading where id = :id").setParameter("id", new Long(id)).executeUpdate();
            transacao.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transacao.rollback();
        }
    }
