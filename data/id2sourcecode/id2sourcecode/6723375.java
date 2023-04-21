    public static void init() throws SQLException {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
        }
        if (new File(filepath).exists() == false) {
            LOG.warn("data file not exists, start creating table ...");
            conn = DriverManager.getConnection(jdbcUrl);
            createTable();
        } else {
            conn = DriverManager.getConnection(jdbcUrl);
        }
        if (needMigrate) {
            LOG.warn("merging old data ...");
            migrateData();
            new File(System.getProperty("user.home") + "/chenance/").renameTo(new File(System.getProperty("user.home") + "/chenance.bak/"));
            LOG.warn("migration finished!");
        }
        String oldVer = getLocalDataVersion();
        String newVer = getCurrentDataVersion();
        if (Float.valueOf(oldVer) < Float.valueOf(newVer)) {
            try {
                FileUtils.copyFile(new File(filepath), new File(filepath + ".bak"));
            } catch (IOException e) {
                LOG.fatal("Backuping database failed", e);
                throw new SQLException("Backuping database failed", e);
            }
            updateData(oldVer, newVer);
        }
        try {
            conn.close();
        } catch (SQLException e) {
            LOG.warn(e);
        }
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("hibernate.connection.driver_class", driverClass);
        props.put("hibernate.connection.url", jdbcUrl);
        props.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
        factory = Persistence.createEntityManagerFactory("chenance-data", props);
        if (em == null) {
            openSession();
        }
    }
