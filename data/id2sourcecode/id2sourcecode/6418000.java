    public void testLocalOnlyAccess() throws SQLException {
        Statement st = null;
        Connection authedCon = null;
        Connection authed2Con = null;
        Connection extraCon = null;
        try {
            saSt.executeUpdate("CREATE USER tloa PASSWORD 'localPassword'");
            saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + AuthFunctionUtils.class.getName() + ".nullFn'");
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TLOA", "unusedPassword");
            } catch (SQLException se) {
                fail("Pre-test with normal access-only account failed");
            }
            try {
                extraCon = DriverManager.getConnection(jdbcUrl, "SA", "wrong");
                fail("Permitted access to SA with wrong password");
            } catch (SQLException se) {
            }
            try {
                extraCon = DriverManager.getConnection(jdbcUrl, "SA", "");
            } catch (SQLException se) {
                fail("Pre-test with SA account failed");
            }
            saSt.executeUpdate("ALTER USER tloa SET LOCAL true");
            try {
                authed2Con = DriverManager.getConnection(jdbcUrl, "TLOA", "wrongPassword");
                fail("Permitted access to local-only user with wrong password");
            } catch (SQLException se) {
            }
            try {
                authedCon = DriverManager.getConnection(jdbcUrl, "TLOA", "localPassword");
            } catch (SQLException se) {
                fail("Access to local-only account with local password failed");
            }
        } finally {
            if (extraCon != null) try {
                extraCon.rollback();
                extraCon.close();
            } catch (SQLException se) {
                logger.error("Close of Extra Conn. failed:" + se);
            } finally {
                extraCon = null;
            }
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
            if (authed2Con != null) try {
                authed2Con.rollback();
                authed2Con.close();
            } catch (SQLException se) {
                logger.error("Close of Authed Conn. #2 failed:" + se);
            } finally {
                authed2Con = null;
            }
        }
    }
