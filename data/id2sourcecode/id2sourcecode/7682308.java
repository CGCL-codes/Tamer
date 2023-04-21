    public void removePrice(Price price) throws PersistenceException {
        logger.info("Removing price...");
        if (price == null) {
            String error = "Price is null.";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from Price p where " + " p = :price ");
            query.setParameter("price", price);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception ex) {
            String error = "Error removing price: " + ex.getMessage();
            logger.error(error);
            em.getTransaction().rollback();
            throw new PersistenceException(ex);
        } finally {
            em.close();
        }
        logger.info("Price removed successfully.");
    }
