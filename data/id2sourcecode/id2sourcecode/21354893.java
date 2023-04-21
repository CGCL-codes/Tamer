    public void testStaleVersionedInstanceFoundOnLock() {
        if (!readCommittedIsolationMaintained("repeatable read tests")) {
            return;
        }
        if (getDialect().doesReadCommittedCauseWritersToBlockReaders()) {
            reportSkip("lock blocking", "stale versioned instance");
            return;
        }
        String check = "EJB3 Specification";
        Session s1 = getSessions().openSession();
        Transaction t1 = s1.beginTransaction();
        Item item = new Item(check);
        s1.save(item);
        t1.commit();
        s1.close();
        Long itemId = item.getId();
        long initialVersion = item.getVersion();
        s1 = getSessions().openSession();
        t1 = s1.beginTransaction();
        item = (Item) s1.get(Item.class, itemId);
        Session s2 = getSessions().openSession();
        Transaction t2 = s2.beginTransaction();
        Item item2 = (Item) s2.get(Item.class, itemId);
        item2.setName("EJB3 Persistence Spec");
        t2.commit();
        s2.close();
        s1.lock(item, LockMode.READ);
        item2 = (Item) s1.get(Item.class, itemId);
        assertTrue(item == item2);
        assertEquals("encountered non-repeatable read", check, item2.getName());
        assertEquals("encountered non-repeatable read", initialVersion, item2.getVersion());
        try {
            s1.lock(item, LockMode.UPGRADE);
            fail("expected UPGRADE lock failure");
        } catch (StaleObjectStateException expected) {
        } catch (SQLGrammarException t) {
            if (getDialect() instanceof SQLServerDialect) {
                t1.rollback();
                t1 = s1.beginTransaction();
            } else {
                throw t;
            }
        }
        t1.commit();
        s1.close();
        s1 = getSessions().openSession();
        t1 = s1.beginTransaction();
        s1.createQuery("delete Item").executeUpdate();
        t1.commit();
        s1.close();
    }
