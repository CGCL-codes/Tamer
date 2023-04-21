    public static Factory hireWorkers(URL url, Factory parent) throws IOException {
        Factory factory = new Factory(parent);
        Map<String, String> props = new HashMap<String, String>();
        InputStream is = url.openStream();
        try {
            PropertiesUtils.load(is, props);
        } finally {
            is.close();
        }
        for (Entry<? extends String, ? extends String> entry : props.entrySet()) {
            String key = entry.getKey();
            factory.setManager(key, new Singleton(entry.getValue(), url + " " + key));
        }
        return factory;
    }
