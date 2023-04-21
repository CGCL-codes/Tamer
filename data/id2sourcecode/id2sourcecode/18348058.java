    public static String stringFromURL(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            return ERXFileUtilities.stringFromInputStream(is);
        } finally {
            is.close();
        }
    }
