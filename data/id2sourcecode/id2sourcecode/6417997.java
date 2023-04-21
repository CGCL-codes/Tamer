    public void testLocalUserAccountLocalRoles() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("CREATE USER tlualr PASSWORD 'wontuse'");
            saSt.executeUpdate("GRANT role3 TO tlualr");
            saSt.executeUpdate("GRANT role4 TO tlualr");
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + AuthFunctionUtils.class.getName() + ".nullFn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TLUALR", "unusedPassword");
            } catch (SQLException se) {
                fail("Access with 'nullFn' failed");
            }
            st = authedCon.createStatement();
            assertTrue("Negative test #1 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t1 VALUES(1)"));
            assertTrue("Negative test #2 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t2 VALUES(2)"));
            assertFalse("Positive test #3 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t3 VALUES(3)"));
            assertFalse("Positive test #4 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t4 VALUES(4)"));
            assertEquals(roles34Set, AuthUtils.getEnabledRoles(authedCon));
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
