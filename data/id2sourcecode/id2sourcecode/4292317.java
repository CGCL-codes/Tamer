    public Configuration configure(URL url) throws HibernateException {
        log.info("configuring from url: " + url.toString());
        try {
            return doConfigure(url.openStream(), url.toString());
        } catch (IOException ioe) {
            throw new HibernateException("could not configure from URL: " + url, ioe);
        }
    }
