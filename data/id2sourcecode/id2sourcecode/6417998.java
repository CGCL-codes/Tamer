    public void testLocalUserAccountLocalRemote0Roles() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("CREATE USER tlualr0r PASSWORD 'wontuse'");
            saSt.executeUpdate("GRANT role3 TO tlualr0r");
            saSt.executeUpdate("GRANT role4 TO tlualr0r");
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + AuthFunctionUtils.class.getName() + ".noRoleFn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TLUALR0R", "unusedPassword");
            } catch (SQLException se) {
                fail("Access with 'noRoleFn' failed");
            }
            st = authedCon.createStatement();
            assertTrue("Negative test #1 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t1 VALUES(1)"));
            assertTrue("Negative test #2 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t2 VALUES(2)"));
            assertTrue("Negative test #3 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t3 VALUES(3)"));
            assertTrue("Negative test #4 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t4 VALUES(4)"));
            assertEquals(0, AuthUtils.getEnabledRoles(authedCon).size());
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
