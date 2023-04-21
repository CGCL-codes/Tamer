    public boolean openConnectionNoDB() {
        return openConnectionImpl("com.mysql.jdbc.Driver", "jdbc:mysql://" + url + ":" + port, "root", "sa-account-password");
    }
