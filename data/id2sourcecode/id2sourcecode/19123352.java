    public void atualizarFuncionarioAtivo(FuncionarioSistema funcionario) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Query query = em.createNativeQuery("UPDATE Funcionario SET ativo ='" + funcionario.isAtivo() + "' WHERE(matricula ='" + funcionario.getMatricula() + "')");
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("bloco finally");
            if (em != null) {
                em.close();
            }
        }
    }
