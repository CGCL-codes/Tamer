    public static <T> T getHeader(URL url, String headerName, Class<T> expectedType) {
        URLConnection connection = null;
        try {
            return ReflectionUtil.coerce(expectedType, (connection = url.openConnection()).getHeaderField("Content-Length"));
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        } finally {
            try {
                connection.getInputStream().close();
            } catch (IOException e) {
            }
        }
    }
