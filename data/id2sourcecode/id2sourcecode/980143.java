    public static ByteBuffer readShaderBinary(Class context, String path) {
        try {
            URL url = IOUtil.getResource(context, path);
            if (url == null) {
                return null;
            }
            return IOUtil.copyStream2ByteBuffer(new BufferedInputStream(url.openStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
