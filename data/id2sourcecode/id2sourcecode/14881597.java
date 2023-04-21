    @Test
    public void testImpliesPermission() {
        gpc1_.add(new ServiceInvocationPermission("/test", "*"));
        gpc1_.add(new RuntimePermission("test"));
        gpc1_.add(new FilePermission("/", "read,write"));
        assertTrue(gpc1_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc1_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertTrue(gpc1_.implies(new FilePermission("/", "read")));
        assertFalse(gpc1_.implies(new FilePermission("/test", "read")));
        assertTrue(gpc1_.implies(new RuntimePermission("test")));
        assertFalse(gpc1_.implies(new RuntimePermission("test1")));
        gpc2_.add(new ServiceInvocationPermission("/test", "*"));
        assertTrue(gpc2_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc2_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertFalse(gpc2_.implies(new FilePermission("/", "read")));
        gpc3_.add(new ServiceInvocationPermission("/test", "*"));
        gpc3_.add(new FilePermission("/", "read,write"));
        assertTrue(gpc3_.implies(new ServiceInvocationPermission("/test", "abc")));
        assertFalse(gpc3_.implies(new ServiceInvocationPermission("/test1", "abc")));
        assertTrue(gpc3_.implies(new FilePermission("/", "read")));
        assertFalse(gpc3_.implies(new FilePermission("/test", "read")));
        assertFalse(gpc3_.implies(new RuntimePermission("test")));
    }
