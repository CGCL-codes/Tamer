    public CRL loadCrl(String crlUrl) throws SSLContextFactoryException, IOException, MalformedURLException {
        InputStream is = null;
        try {
            URL url = new URL(crlUrl);
            is = url.openStream();
            return loadCrl(new BufferedInputStream(is));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
