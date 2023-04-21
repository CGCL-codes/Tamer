    public static Properties getAiPlayers() {
        Properties props = new Properties();
        try {
            URL url = res.getClass().getResource("AiPlayers.properties");
            if (url == null) return null;
            InputStream in = url.openStream();
            props.load(in);
            in.close();
        } catch (IOException ioe) {
            return null;
        }
        return props;
    }
