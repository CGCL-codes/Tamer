    public static void loadPropertiesMap(Map<String, Object> result, String propertiesURI) throws IOException {
        URL url;
        Properties props = new Properties();
        Reader in = null;
        try {
            url = new URL(propertiesURI);
            URLConnection urlConnect = url.openConnection();
            in = new InputStreamReader(urlConnect.getInputStream());
        } catch (MalformedURLException e) {
            in = new InputStreamReader(new FileInputStream(propertiesURI));
        }
        props.load(in);
        for (Enumeration<?> enumeration = props.keys(); enumeration.hasMoreElements(); ) {
            String key = enumeration.nextElement().toString();
            Object o = PropertiesParser.getPropertyValue(props.getProperty(key));
            result.put(key, o);
        }
    }
