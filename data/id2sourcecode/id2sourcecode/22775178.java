    @SuppressWarnings("unchecked")
    private static void delete(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Query query1 = entityManager.createQuery("SELECT p FROM Person p");
            Collection<Person> collection1 = (Collection<Person>) query1.getResultList();
            for (Person person : collection1) {
                entityManager.remove(person);
            }
            Query query2 = entityManager.createQuery("DELETE FROM Address");
            query2.executeUpdate();
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            entityManager.close();
        }
    }
