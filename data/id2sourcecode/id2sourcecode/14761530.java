    public XMLResourceBundle(String baseName, Locale locale, ClassLoader classloader) {
        this.locale = locale;
        if (loader == null) loader = ClassLoader.getSystemClassLoader(); else loader = classloader;
        if (locale == null) locale = Locale.getDefault();
        props = new Properties();
        try {
            URL url = loader.getResource(baseName + locale.getLanguage() + "_" + locale.getCountry() + SUFFIX);
            if (url == null) {
                url = loader.getResource(baseName + "_" + locale.getLanguage() + SUFFIX);
                if (url == null) url = loader.getResource(baseName + SUFFIX);
            }
            URLConnection connection = url.openConnection();
            props.loadFromXML(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
