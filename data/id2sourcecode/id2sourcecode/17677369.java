    public void setup() {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource("oscache.properties");
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            logger.error("Properties are not loaded. Working with defaults.");
        }
        admin = new GeneralCacheAdministrator(props);
        map = admin.getCache();
        if (props.get("item.expire") != null) {
            try {
                secondsExpire = Integer.parseInt((String) props.get("item.expire"));
            } catch (Exception e) {
                logger.warn("Wrong format for item.expire", e);
            }
        }
        logger.info("Cache expiration in seconds: " + secondsExpire);
    }
