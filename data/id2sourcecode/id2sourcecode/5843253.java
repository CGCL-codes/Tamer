    protected UniResourceBundle createUniResourceBundle(String localeFileName) throws IOException {
        UniResourceBundle localBundle = null;
        URL resource = getUrl(localeFileName);
        if (resource == null) {
            return null;
        }
        URLConnection urlConnection = resource.openConnection();
        if (urlConnection.getDoInput()) {
            Properties properties = new Properties();
            properties.load(urlConnection.getInputStream());
            localBundle = new UniResourceBundle(properties);
        }
        return localBundle;
    }
