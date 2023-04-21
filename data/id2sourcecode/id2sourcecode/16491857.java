    public boolean openConnection(String url, String userPass) throws SQLException {
        return openConnection(url, userPass, '/');
    }
