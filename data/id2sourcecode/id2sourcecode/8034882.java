    public boolean isDocumentAvailable(URI docuri) {
        final URL docurl;
        try {
            docurl = docuri.toURL();
        } catch (MalformedURLException e) {
            return false;
        }
        if (_sharedCache.containsKey(docurl)) {
            return true;
        } else {
            final InputStream is;
            try {
                is = docurl.openStream();
            } catch (IOException e) {
                return false;
            }
            try {
                if (is.available() > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
    }
