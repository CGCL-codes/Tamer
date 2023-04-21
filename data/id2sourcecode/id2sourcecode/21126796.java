    protected InputStream getResource(String location) {
        if (location == null) {
            return null;
        }
        if (location.startsWith("web:")) {
            if (this.servletContext == null) {
                return null;
            }
            location = location.substring(4);
            return this.servletContext.getResourceAsStream(location);
        }
        if (location.startsWith("classpath:")) {
            location = location.substring(10);
            return this.getClass().getResourceAsStream(location);
        } else if (location.startsWith("http:") || location.startsWith("https:")) {
            try {
                URL url = new URL(location);
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                throw new CannotRetrieveResource(e);
            }
        }
        throw new CannotRetrieveResource();
    }
