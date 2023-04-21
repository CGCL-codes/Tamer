    @Test
    public void testAddPermission() {
        try {
            gpc1_.add(new ServiceInvocationPermission("/test", "*"));
            gpc1_.add(new RuntimePermission("test"));
            gpc1_.add(new FilePermission("/", "read,write"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc2_.add(new ServiceInvocationPermission("/test", "*"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc2_.add(new RuntimePermission("test"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc2_.add(new FilePermission("/", "read,write"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc3_.add(new ServiceInvocationPermission("/test", "*"));
        } catch (IllegalArgumentException e) {
            fail();
        }
        try {
            gpc3_.add(new RuntimePermission("test"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            gpc3_.add(new FilePermission("/", "read,write"));
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
