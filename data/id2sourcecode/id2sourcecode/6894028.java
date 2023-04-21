    private void loadLanguage() {
        URL url = getClass().getClassLoader().getResource("language/" + _language + ".properties");
        _properties = new Properties();
        _log.debug("Url : " + url);
        try {
            _properties.load(url.openStream());
        } catch (IOException e) {
            _log.error("Impossible de charger " + _language + ".properties");
        }
    }
