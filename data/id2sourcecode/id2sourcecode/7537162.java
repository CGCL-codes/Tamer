    public void testCreateChildDataContext() {
        DataContext parent = createDataContext();
        parent.setValidatingObjectsOnCommit(true);
        DataContext child1 = parent.createChildDataContext();
        assertNotNull(child1);
        assertSame(parent, child1.getChannel());
        assertTrue(child1.isValidatingObjectsOnCommit());
        parent.setValidatingObjectsOnCommit(false);
        DataContext child2 = parent.createChildDataContext();
        assertNotNull(child2);
        assertSame(parent, child2.getChannel());
        assertFalse(child2.isValidatingObjectsOnCommit());
        DataContext child21 = child2.createChildDataContext();
        assertNotNull(child21);
        assertSame(child2, child21.getChannel());
        assertFalse(child2.isValidatingObjectsOnCommit());
    }
