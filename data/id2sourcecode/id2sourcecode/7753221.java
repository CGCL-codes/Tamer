    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "equals", args = { java.lang.Object.class })
    public void test_equalsLjava_lang_Object() {
        PropertyPermission equalToJavaPP = new PropertyPermission("java.*", "read");
        PropertyPermission notEqualToJavaPP = new PropertyPermission("java.*", "read, write");
        PropertyPermission alsoNotEqualToJavaPP = new PropertyPermission("java.home", "read");
        assertTrue("Equal returned false for equal objects", javaPP.equals(equalToJavaPP));
        assertTrue("Equal returned true for objects with different names", !javaPP.equals(notEqualToJavaPP));
        assertTrue("Equal returned true for objects with different permissions", !javaPP.equals(alsoNotEqualToJavaPP));
    }
