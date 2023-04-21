    public void excluirProduto(Produto produto) throws PersistenciaException {
        if (produto == null) {
            String erro = "Erro ao tentar excluir produto. Produto esta nulo.";
            logger.error(erro);
            throw new IllegalArgumentException(erro);
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from Produto p where p.cdProduto = :id").setParameter("id", produto.getCdProduto());
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            String erro = "Erro ao tentar excluir produto.";
            logger.error(erro);
            em.getTransaction().rollback();
            throw new PersistenciaException(e);
        } finally {
            em.close();
        }
    }
