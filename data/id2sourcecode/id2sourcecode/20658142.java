    public static InputStream openNamedResource(String resourceURL) throws java.io.IOException {
        InputStream in = null;
        boolean result = false;
        boolean httpURL = false;
        URL propsURL = null;
        try {
            propsURL = new URL(resourceURL);
        } catch (MalformedURLException ex) {
            propsURL = null;
        }
        if (propsURL == null) {
            propsURL = ResourceUtilities.class.getResource(resourceURL);
            if (propsURL == null && resourceURL.startsWith("FILE:")) {
                try {
                    in = new FileInputStream(resourceURL.substring(5));
                    return in;
                } catch (FileNotFoundException ex) {
                    in = null;
                    propsURL = null;
                }
            }
        } else {
            String protocol = propsURL.getProtocol();
            httpURL = protocol.equals("http");
        }
        if (propsURL != null) {
            URLConnection urlConn = propsURL.openConnection();
            if (httpURL) {
                String hdrVal = urlConn.getHeaderField(0);
                if (hdrVal != null) {
                    String code = HTTPUtilities.getResultCode(hdrVal);
                    if (code != null) {
                        if (!code.equals("200")) {
                            throw new java.io.IOException("status code = " + code);
                        }
                    }
                }
            }
            in = urlConn.getInputStream();
        }
        if (in == null) throw new java.io.IOException("could not locate resource '" + resourceURL + "'");
        return in;
    }
