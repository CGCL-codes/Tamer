    public void testSimpleWriteAndReadObject() throws Exception {
        TestObj obj = new TestObj();
        obj.setI(324);
        persistentObject.initialize(obj);
        AggregatedTransaction aggregatedTransaction = ArchiveContext.get().getAggregatedTransaction();
        aggregatedTransaction.setComplete(true);
        PersistentObject readPersistentObject = writeAndReadPersistentObject(persistentObject);
        TestObj readObj = (TestObj) readPersistentObject.findTargetObject();
        assertNotNull(readObj);
        assertEquals(obj.getI(), readObj.getI());
        assertSame(persistentObject, aggregatedTransaction.resolveObject(persistentObject));
    }
