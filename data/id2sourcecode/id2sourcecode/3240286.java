    public static byte[] loadBytesFromURL(URL url) throws IOException {
        URLConnection con = url.openConnection();
        int size = con.getContentLength();
        InputStream in = con.getInputStream();
        if (in != null) {
            try {
                return (size != -1) ? loadBytesFromStreamForSize(in, size) : loadBytesFromStream(in);
            } finally {
                try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
        }
        return null;
    }
