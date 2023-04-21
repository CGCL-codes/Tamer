    public void deletarParagrafo(final Paragrafo... Paragrafos) throws HibernateException, Exception {
        for (Paragrafo Paragrafo : Paragrafos) {
            Session session = Hibernate.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                String sqlDeletaLocador = "DELETE FROM bdappcontrato.clasulaparagrafopk WHERE codParagrafo = :id ;";
                SQLQuery createSQLQuery = session.createSQLQuery(sqlDeletaLocador);
                createSQLQuery.setInteger("id", Paragrafo.getCodParagrafo());
                createSQLQuery.executeUpdate();
                session.delete(Paragrafo);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException("HIBERNATE Erro no Deletar Paragrafo: ", e);
            } catch (Exception e) {
                throw new Exception("GERAL Erro no Deletar Paragrafo: ", e);
            }
        }
    }
