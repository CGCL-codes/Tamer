    public void deletarImovel(final Imovel... imoveis) throws Exception {
        for (Imovel imovel : imoveis) {
            Session session = Hibernate.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                String sqlDeletaLocador = "DELETE FROM locadorimovelpk WHERE codImovel = :id ;";
                SQLQuery createSQLQuery = session.createSQLQuery(sqlDeletaLocador);
                createSQLQuery.setInteger("id", imovel.getCodImovel());
                System.out.println(createSQLQuery.executeUpdate());
                session.delete(imovel);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException("HIBERNATE Erro no Deletar Imovel: ", e);
            } catch (Exception e) {
                throw new Exception("GERAL Erro no Deletar Imovel: ", e);
            }
        }
    }
