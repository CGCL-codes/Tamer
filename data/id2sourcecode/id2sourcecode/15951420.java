    protected InputStream getInputStream(String filename) throws PersistenceException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        if (in == null) {
            try {
                in = new FileInputStream(filename);
            } catch (IOException ex) {
                log.debug("Did not load InputStream as resource: filename=" + filename);
            }
        }
        if (in == null) {
            try {
                URL url = new URL(filename);
                in = url.openStream();
            } catch (Exception ex) {
                log.debug("Did not load InputStream as URL: filename=" + filename);
            }
        }
        return in;
    }
