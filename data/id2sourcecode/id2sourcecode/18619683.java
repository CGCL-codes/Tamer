    private static OracleConnection getOracleConnection(String server, String port, String sid, String userid, String pwd) throws SQLException {
        String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + sid;
        return (OracleConnection) openConnection("oracle.jdbc.driver.OracleDriver", url, userid, pwd);
    }
