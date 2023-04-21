    @Override
    public int removeById(Integer id) {
        EntityTransaction transaction = this.entityManager.getTransaction();
        int ret = 0;
        try {
            transaction.begin();
            ret = entityManager.createQuery("delete from Person p where p.id = :id").setParameter("id", id).executeUpdate();
            this.entityManager.flush();
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
        }
        return ret;
    }
