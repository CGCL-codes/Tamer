    private Properties _getJDBCProperties() {
        Properties jdbcProps = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            URL url = classLoader.getResource("jdbc.properties");
            if (url != null) {
                InputStream is = url.openStream();
                jdbcProps.load(is);
                is.close();
                System.out.println("Loading " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            URL url = classLoader.getResource("jdbc-ext.properties");
            if (url != null) {
                InputStream is = url.openStream();
                jdbcProps.load(is);
                is.close();
                System.out.println("Loading " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jdbcProps;
    }
