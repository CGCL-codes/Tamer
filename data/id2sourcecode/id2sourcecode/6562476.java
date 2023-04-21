    public boolean openConnection() {
        return openConnectionImpl("com.mysql.jdbc.Driver", "jdbc:mysql://" + url + ":" + port + "/" + database, "root", "sa-account-password");
    }
