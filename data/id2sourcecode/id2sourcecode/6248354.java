    private static Properties load() throws DBException {
        Properties props = new Properties() {

            String computerName = "";

            {
                try {
                    computerName = java.net.InetAddress.getLocalHost().getHostName();
                } catch (Exception e) {
                }
            }

            @Override
            public String getProperty(String key) {
                String val = super.getProperty(computerName + "." + key);
                if (val != null) return val; else return super.getProperty(key);
            }
        };
        try {
            URL url = ClassLoader.getSystemResource(ConnectionFactory.configPath);
            props.load(url.openStream());
        } catch (IOException e) {
            throw new DBException("Could not load config file - " + e.getMessage());
        }
        return props;
    }
