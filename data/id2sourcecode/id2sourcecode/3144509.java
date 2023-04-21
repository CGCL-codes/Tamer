    public void excluir(PrecoProduto precoProduto) throws PersistenciaException {
        if (precoProduto == null) {
            String erro = "Erro ao tentar excluir preco: argumento nulo.";
            logger.error(erro);
            throw new IllegalArgumentException(erro);
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from PrecoProduto p where p.id = :id");
            query.setParameter("id", new PrecoProdutoPK(precoProduto.getEstabelecimento().getCdPessoa(), precoProduto.getProduto().getCdProduto()));
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            String erro = "Erro ao tentar excluir preco.";
            logger.error(erro);
            em.getTransaction().rollback();
            throw new PersistenciaException(e);
        } finally {
            em.close();
        }
    }
