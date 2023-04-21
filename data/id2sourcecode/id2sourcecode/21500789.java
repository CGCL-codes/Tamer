    public void testUpdateSimple() {
        try {
            EntityManager em = getEM();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Person p = new Person(101, "Fred", "Flintstone", "fred.flintstone@jpox.com");
                em.persist(p);
                em.flush();
                Query q = em.createQuery("UPDATE Person p SET p.emailAddress = :param WHERE p.firstName = 'Fred'");
                q.setParameter("param", "fred@flintstones.com");
                int val = q.executeUpdate();
                assertEquals("Number of records updated by query was incorrect", 1, val);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
                em.close();
            }
        } finally {
            clean(Person.class);
        }
    }
