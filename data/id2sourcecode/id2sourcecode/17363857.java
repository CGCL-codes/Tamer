    public void excluirEstabelecimento(Estabelecimento estabelecimento) throws PersistenciaException {
        if (estabelecimento == null) {
            String erro = "Erro ao tentar excluir estabelecimento: argumento nulo.";
            logger.error(erro);
            throw new IllegalArgumentException(erro);
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from Pessoa e where e.cdPessoa = :id").setParameter("id", estabelecimento.getCdPessoa());
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            String erro = "Erro ao tentar excluir estabelecimento.";
            logger.error(erro);
            em.getTransaction().rollback();
            throw new PersistenciaException(e);
        } finally {
            em.close();
        }
    }
