    public void testPullSchema() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("CREATE USER tps PASSWORD 'wontuse'");
            saSt.executeUpdate("CREATE SCHEMA s1");
            saSt.executeUpdate("CREATE SCHEMA s2");
            saSt.executeUpdate("ALTER USER tps SET INITIAL SCHEMA s1");
            saSt.executeUpdate("CREATE TABLE s1.s1t1 (i INTEGER)");
            saSt.executeUpdate("GRANT ALL ON s1.s1t1 TO public");
            saSt.executeUpdate("CREATE TABLE s2.s2t1 (i INTEGER)");
            saSt.executeUpdate("GRANT ALL ON s2.s2t1 TO public");
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + getClass().getName() + ".schemaS2Fn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TPS", "unusedPassword");
            } catch (SQLException se) {
                fail("Access with 'schemaS2Fn' failed");
            }
            st = authedCon.createStatement();
            assertTrue("Negative test #1 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO s1t1 VALUES(1)"));
            assertFalse("Positive test #2 failed", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO s2t1 VALUES(2)"));
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
