    public String setProperties(URL url, boolean inclName) throws IOException {
        String name = null;
        if (url != null) {
            InputStream uis = url.openStream();
            try {
                name = this._setProperties(uis, inclName, url);
            } finally {
                try {
                    uis.close();
                } catch (IOException ioe) {
                }
            }
        }
        return name;
    }
