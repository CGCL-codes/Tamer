    public static <T> T readURL(URL url, StreamDecoder<T> codec) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            T data = codec.decode(stream);
            return data;
        } finally {
            IOHelper.closeQuietly(stream);
        }
    }
