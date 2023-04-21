    @Test
    public void testConfig() throws Exception {
        JDBCRealmFake jdbcRealm = new JDBCRealmFake();
        jdbcRealm.init(authConfig);
        assertTrue(authConfig.isPassLocalRequests());
        assertEquals(USER, jdbcRealm.getConnectionName());
        assertEquals(PASSWORD, jdbcRealm.getConnectionPassword());
        assertEquals(JDBC_MEM_TESTEDB_URL, jdbcRealm.getConnectionURL());
        assertEquals(HSQLDB_JDBC_DRIVER, jdbcRealm.getDriverName());
        assertEquals("groname", jdbcRealm.getRoleNameCol());
        assertEquals("passwd", jdbcRealm.getUserCredCol());
        assertEquals("username", jdbcRealm.getUserNameCol());
        assertEquals("usegroup", jdbcRealm.getUserRoleTable());
        assertEquals("users", jdbcRealm.getUserTable());
        assertEquals(true, jdbcRealm.isHasMessageDigest());
        assertEquals("f52181e0b1065054f15ff683e4519085", jdbcRealm.digest("Bla-Bla-Bla"));
    }
