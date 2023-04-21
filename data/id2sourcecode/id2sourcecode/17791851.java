    public PFBData parsePFB(java.net.URL url) throws IOException {
        InputStream in = url.openStream();
        try {
            return parsePFB(in);
        } finally {
            in.close();
        }
    }
