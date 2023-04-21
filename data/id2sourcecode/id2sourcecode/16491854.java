    public boolean openConnection(String url) throws SQLException {
        try {
            Class.forName(RunConfig.getInstance().getDriverNameJDBC());
            if (url == null) return openConnection();
            connection = DriverManager.getConnection(url);
            if (statementTable == null) statementTable = new Hashtable<String, PreparedStatement>();
            if (resultTable == null) resultTable = new Hashtable<String, ResultSet>();
            clearStatus();
            return true;
        } catch (Exception e) {
            setStatus(e);
            return false;
        }
    }
