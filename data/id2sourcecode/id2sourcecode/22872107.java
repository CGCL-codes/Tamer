    @Test
    public void testSimpleCrossTxnWork() {
        Session session1 = injector.getInstance(SessionFactory.class).openSession();
        ManagedSessionContext.bind(session1);
        final TransactionalObject txnal = injector.getInstance(TransactionalObject.class);
        final HibernateTestEntity entity = txnal.runOperationInTxn();
        txnal.id = entity.getId();
        assert session1.contains(entity) : "Entity was not persisted!";
        assert UNIQUE_TEXT_PERSISTENT.equals(entity.getText()) : "entity was not stored correctly";
        txnal.runReadOnlyTxn();
        assert UNIQUE_TEXT_TRANSIENT.equals(entity.getText()) : "entity dirty state was not modified in read-only txn";
        Query query = session1.createQuery("from HibernateTestEntity where text = :text");
        query.setParameter("text", UNIQUE_TEXT_TRANSIENT);
        assert null == query.uniqueResult() : "Text from read-only txn was found in persistent store!";
        query = session1.createQuery("from HibernateTestEntity where text = :text");
        query.setParameter("text", UNIQUE_TEXT_PERSISTENT);
        assert null != query.uniqueResult() : "Read-only txn affected persistent store!";
        txnal.runReadWriteTxn();
        assert UNIQUE_TEXT_PERSISTENT2.equals(entity.getText()) : "entity was not modified in read-write txn!!";
        query = session1.createQuery("from HibernateTestEntity where text = :text");
        query.setParameter("text", UNIQUE_TEXT_PERSISTENT);
        assert null == query.uniqueResult() : "Text from original txn was found in persistent store!";
        txnal.runReadOnlyTxn();
        assert UNIQUE_TEXT_TRANSIENT.equals(entity.getText()) : "entity appears modified in read-only txn!!";
        query = session1.createQuery("from HibernateTestEntity where text = :text");
        query.setParameter("text", UNIQUE_TEXT_TRANSIENT);
        assert null == query.uniqueResult() : "Text from read-only txn was found in persistent store!";
        session1.close();
        session1 = injector.getInstance(SessionFactory.class).openSession();
        ManagedSessionContext.bind(session1);
        query = session1.createQuery("from HibernateTestEntity where text = :text");
        query.setParameter("text", UNIQUE_TEXT_PERSISTENT2);
        final HibernateTestEntity persistentCopy = (HibernateTestEntity) query.uniqueResult();
        assert null != persistentCopy : "Text from read-only txn was found in persistent store!";
        System.out.println(persistentCopy.getText());
        assert UNIQUE_TEXT_PERSISTENT2.equals(persistentCopy.getText()) : "Persistent copy of entity appears modified by read-only txn";
        session1.close();
    }
