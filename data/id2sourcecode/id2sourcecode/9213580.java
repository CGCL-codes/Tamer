    private PersistenceManager get(Class<?> clazz) {
        String pack = getPackage(clazz);
        PersistenceManager pm = configuration.get(pack);
        if (pm != null) return pm;
        URL url = clazz.getResource("siena.properties");
        if (url == null) throw new SienaException("Cannot load siena.properties file for package: " + pack);
        Properties p = new Properties();
        try {
            p.load(url.openStream());
        } catch (IOException e) {
            throw new SienaException(e);
        }
        String prefix = "siena." + pack + ".";
        Properties sysprops = System.getProperties();
        for (Map.Entry<Object, Object> entry : sysprops.entrySet()) {
            String key = entry.getKey().toString();
            if (key.startsWith(prefix)) {
                String value = entry.getValue().toString();
                p.setProperty(key.substring(prefix.length()), value);
            }
        }
        String impl = p.getProperty("implementation");
        if (impl == null) throw new SienaException("key 'implementation' not found at " + url);
        try {
            pm = (PersistenceManager) Class.forName(impl).newInstance();
            pm.init(p);
        } catch (Exception e) {
            throw new SienaException("Error while creating instance of: " + impl, e);
        }
        configuration.put(pack, pm);
        return pm;
    }
