    public void atualizarSenhaFuncionario(FuncionarioSistema funcionario) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createNativeQuery("UPDATE Funcionario SET senha ='" + funcionario.getSenha() + "' WHERE(matricula ='" + funcionario.getMatricula() + "')");
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
