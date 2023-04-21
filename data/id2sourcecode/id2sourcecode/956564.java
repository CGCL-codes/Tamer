    public static Connection openConnection(File jdbcJar, String driverClass, String url, String username, String password) throws SQLException, IOException, ClassNotFoundException {
        JarLoader.addFile(jdbcJar);
        Class.forName(driverClass);
        return DriverManager.getConnection(url, username, password);
    }
