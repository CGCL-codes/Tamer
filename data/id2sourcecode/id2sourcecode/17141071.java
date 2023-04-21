    private void processKeyStore(String urlSpec, String type, URL rootURL) {
        if (type == null) type = KeyStore.getDefaultType();
        InputStream in = null;
        try {
            URL url;
            try {
                url = new URL(urlSpec);
            } catch (MalformedURLException mue) {
                url = new URL(rootURL, urlSpec);
            }
            KeyStore ks = KeyStore.getInstance(type);
            try {
                in = url.openStream();
            } catch (IOException ioe) {
            }
            if (in != null) {
                ks.load(in, null);
                keyStores.add(ks);
            }
        } catch (Exception e) {
            SignedBundleHook.log(e.getMessage(), FrameworkLogEntry.WARNING, e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
