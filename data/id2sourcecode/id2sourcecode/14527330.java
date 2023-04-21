    public static PooledConnectionFactory getConnectionFactory() {
        if (INSTANCE == null) {
            try {
                URL url = PooledConnectionFactory.class.getResource("/se/studieren/dbvote/db/connection.properties");
                Properties properties = new Properties();
                properties.load(url.openStream());
                INSTANCE = new PooledConnectionFactory(properties);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }
