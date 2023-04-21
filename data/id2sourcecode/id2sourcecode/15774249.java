    private static ObjectStreamConstants createSerializerStream(final URL url, final boolean restore) throws IOException {
        if (restore) return new SerializeInputStream(url.openConnection().getInputStream());
        return new SerializeOutputStream(url.openConnection().getOutputStream());
    }
