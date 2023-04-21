    public void testRoot() throws StoreException {
        logger.info("testRoot");
        Object rootObject = store.getRoot(Bob1.class);
        assertNull("read root object must failed", rootObject);
        final Bob1 bob1 = new Bob1();
        store.setRoot(bob1);
        store.save();
        store.checkIntegrity();
        rootObject = store.getRoot(Bob1.class);
        assertNotNull("must have root", rootObject);
        logger.info("root object class " + rootObject.getClass());
        assertTrue("root object must be instance of Bob1", rootObject instanceof Bob1);
        assertNotSame("writed and readed objects must not be same object in memory", bob1, rootObject);
        final Object object = new Object();
        try {
            store.setRoot(object);
            fail("set root again must failed");
        } catch (StoreException exception) {
        }
    }
