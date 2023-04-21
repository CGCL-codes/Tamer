    public void inserirParametros(Integer parcelas, Integer tipoImpressao, String enderecoImpressao) {
        HibernateUtil.closeSession();
        Session session = HibernateUtil.currentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createSQLQuery("update parametros set numeroParcelas = :parcelas, tipo_impressao = :tipoImpressao, endereco_impressora = :enderecoImpressao where id = 1");
            query.setParameter("parcelas", parcelas);
            query.setParameter("tipoImpressao", tipoImpressao);
            query.setParameter("enderecoImpressao", enderecoImpressao);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
