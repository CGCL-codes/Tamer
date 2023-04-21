    private Document fetchHibernateConfigFromURL(URL url) throws Exception {
        try {
            return fetchHibernateConfig(url.openStream());
        } catch (Exception e) {
            throw new Exception("Could not initialize from Hibernate Configuration URL:" + url, e);
        }
    }
