    private static Connection openConnection(String driver, String url, String uid, String pwd) throws SQLException {
        Connection conn = null;
        try {
            Class.forName(driver);
        } catch (java.lang.ClassNotFoundException e) {
            fail(e.getMessage());
        }
        conn = DriverManager.getConnection(url, uid, pwd);
        return conn;
    }
