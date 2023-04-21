    private static Properties loadPropertiesFromClassPath(String name, String suffisso) {
        Properties props = null;
        try {
            props = new Properties();
            URL url = ClassLoader.getSystemResource(name + suffisso);
            props.load(url.openStream());
            return props;
        } catch (Exception e) {
            log.error("Errore nel caricamento come system resource: ", e);
        }
        return null;
    }
