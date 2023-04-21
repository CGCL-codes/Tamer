    public static InputStream open(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
