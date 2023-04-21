    public static void initJMF(URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            if (in == null) {
                throw new IllegalArgumentException("No such jmf properties file: " + url);
            }
            readJMFRegistry(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
