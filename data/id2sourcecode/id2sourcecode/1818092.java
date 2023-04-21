    public void testBulkDelete() {
        try {
            EntityManager em = getEM();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Timer t = new Timer("Seiko", true, null);
                em.persist(t);
                em.flush();
                Query q = em.createQuery("DELETE FROM " + Timer.class.getName() + " t");
                int number = q.executeUpdate();
                assertEquals(1, number);
                tx.rollback();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
                em.close();
            }
        } finally {
            clean(Timer.class);
        }
    }
