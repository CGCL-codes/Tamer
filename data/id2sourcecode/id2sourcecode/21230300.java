    public int hashRabin(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            return hashRabin(is);
        } finally {
            is.close();
        }
    }
