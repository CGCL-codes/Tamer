    public String getText(String key, Locale locale) {
        try {
            URL url = getUrl(TEXT_PROPERTIES);
            PropertyResourceBundle bundle = new PropertyResourceBundle(url.openStream());
            return bundle.getString(key);
        } catch (Exception e) {
            return "[" + key + "]";
        }
    }
