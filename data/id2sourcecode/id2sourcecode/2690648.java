    public ArrayList<Entry> read(URL url) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            return read(stream);
        } finally {
            StreamUtility.close(stream);
        }
    }
