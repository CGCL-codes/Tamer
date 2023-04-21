    public boolean openConnection(String url, String user, String pass) throws SQLException {
        try {
            Class.forName(RunConfig.getInstance().getDriverNameJDBC());
            if (url == null) url = RunConfig.getInstance().getConnectionUrlJDBC();
            if (user == null) user = RunConfig.getInstance().getUserNameJDBC();
            if (pass == null) pass = RunConfig.getInstance().getPasswordJDBC();
            connection = DriverManager.getConnection(url, user, pass);
            if (statementTable == null) statementTable = new Hashtable<String, PreparedStatement>();
            if (resultTable == null) resultTable = new Hashtable<String, ResultSet>();
            clearStatus();
            return true;
        } catch (Exception e) {
            setStatus(e);
            return false;
        }
    }
