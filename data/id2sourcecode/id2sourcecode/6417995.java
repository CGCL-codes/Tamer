    public void testRemoteAccountRemoteRoles() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + getClass().getName() + ".twoRolesFn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "zeno", "a password");
            } catch (SQLException se) {
                fail("Access with 'twoRolesFn' failed");
            }
            st = authedCon.createStatement();
            assertFalse("Positive test failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t1 VALUES(1)"));
            assertTrue("Negative test failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t3 VALUES(3)"));
            assertEquals(twoRolesSet, AuthUtils.getEnabledRoles(authedCon));
        } finally {
            if (st != null) try {
                st.close();
            } catch (SQLException se) {
                logger.error("Close of Statement failed:" + se);
            } finally {
                st = null;
            }
            if (authedCon != null) try {
                authedCon.rollback();
                authedCon.close();
            } catch (SQLException se) {
                logger.error("Close of Authed Conn. failed:" + se);
            } finally {
                authedCon = null;
            }
        }
    }
