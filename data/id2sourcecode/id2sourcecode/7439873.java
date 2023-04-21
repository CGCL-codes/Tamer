    public TextureData newTextureData(GLProfile glp, URL url, int internalFormat, int pixelFormat, boolean mipmap, String fileSuffix) throws IOException {
        InputStream stream = url.openStream();
        try {
            return newTextureData(glp, stream, internalFormat, pixelFormat, mipmap, fileSuffix);
        } finally {
            stream.close();
        }
    }
