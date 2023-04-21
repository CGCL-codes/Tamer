    public void testVirtualAccount() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        try {
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + AuthFunctionUtils.class.getName() + ".nullFn'");
            saSt.executeUpdate("GRANT ALL ON t1 TO public");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "VIRTUALUSER", "unusedPassword");
            } catch (SQLException se) {
                fail("Failed to grant access to virtual user");
            }
            st = authedCon.createStatement();
            assertFalse("Virtual user failed to write public-write table", AuthFunctionUtils.updateDoesThrow(st, "INSERT INTO t1 VALUES(2)"));
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
