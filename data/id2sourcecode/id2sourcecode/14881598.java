    @Test
    public void testElements() {
        Set<Permission> permissions = new HashSet<Permission>();
        permissions.add(new ServiceInvocationPermission("/test", "*"));
        permissions.add(new RuntimePermission("test"));
        permissions.add(new FilePermission("/", "read,write"));
        for (Iterator<Permission> itr = permissions.iterator(); itr.hasNext(); ) {
            gpc1_.add(itr.next());
        }
        Enumeration<Permission> elements = gpc1_.elements();
        try {
            while (elements.hasMoreElements()) {
                permissions.remove(elements.nextElement());
            }
        } catch (Exception e) {
            fail();
        }
        assertTrue(permissions.isEmpty());
    }
