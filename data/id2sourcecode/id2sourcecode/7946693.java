    public void testSubClass() {
        super.test();
        assertTrue("super class read field", readTest);
        assertTrue("super class write field", readTest);
    }
