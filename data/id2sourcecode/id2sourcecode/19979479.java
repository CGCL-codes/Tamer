    protected void updateVersionInformation(URL url) throws Exception {
        Properties properties = new Properties();
        properties.load(url.openStream());
        setVersion("" + properties.get("version"));
        setRevision("" + properties.getProperty("revision"));
        setDate(new Date(Long.parseLong(properties.getProperty("date"))));
    }
