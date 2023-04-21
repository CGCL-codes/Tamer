    public boolean connect() {
        try {
            URL url = new URL(catalog_url);
            InputStream in = url.openStream();
            if (!loadInputStream(in)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            this._message = e.getMessage();
            return false;
        }
    }
