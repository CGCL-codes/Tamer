    public void testOK2() {
        EmmetProfileSchema ps = new EmmetProfileSchema();
        ps.setAuthoritiesRegistry(EmmetTestHelper.buildAuthoritiesRegistry());
        HashMap<String, Properties> init = new HashMap<String, Properties>();
        init.put("fred", PropertiesHelper.initProperties("read=ROLE_USER,ROLE_ADMIN\nwrite=\nname=Frederic\ndescription=the great\n"));
        ps.setSchema(init);
        ps.afterPropertiesSet();
        EmmetProfileSchema.PropertyDescriptor desc = ps.lookupProperty("fred");
        assertNotNull(desc);
        assertEquals("fred", desc.getPropName());
        assertEquals("Frederic", desc.getReadableName());
        assertEquals("the great", desc.getDescription());
        assertTrue(desc.getReadRule() instanceof OrRule);
        assertTrue(desc.getWriteRule() instanceof PassRule);
    }
