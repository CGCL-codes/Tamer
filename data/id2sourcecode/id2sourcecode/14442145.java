    public void testTryCopyFields_4args() {
        System.out.println("tryCopyFields");
        Object source = null;
        Object target = null;
        Map<String, Object> readers = null;
        Map<String, Object> writers = null;
        int expResult = 0;
        int result = CloneHelper.tryCopyFields(source, target, readers, writers);
    }
