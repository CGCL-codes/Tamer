    public void testUnresolvedReferenceWriteAndReadObject() throws Exception {
        TestObj obj = new TestObj();
        obj.setI(324);
        persistentObject.initialize(obj);
        AggregatedTransaction aggregatedTransaction = ArchiveContext.get().getAggregatedTransaction();
        lockMock.stubs().method("acquireWriteLock").withAnyArguments();
        enterTransaction();
        PersistentObject readPersistentObject = writeAndReadPersistentObject(persistentObject);
        leaveTransaction();
        TestObj readObj = (TestObj) readPersistentObject.findTargetObject();
        assertNotNull(readObj);
        assertEquals(10, readObj.getI());
        assertNotSame(persistentObject, aggregatedTransaction.resolveObject(persistentObject));
        lockMock.verify();
    }
