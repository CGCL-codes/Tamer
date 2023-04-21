    public InputStream getStream(String uri) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            URL url = context.getExternalContext().getResource(uri);
            return url.openStream();
        } catch (Exception ex) {
            _LOG.severe("OPEN_URI_EXCEPTION", uri);
            _LOG.severe(ex);
            return null;
        }
    }
