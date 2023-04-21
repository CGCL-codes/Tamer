        public TextureData newTextureData(URL url, int internalFormat, int pixelFormat, boolean mipmap, String fileSuffix) throws IOException {
            InputStream stream = url.openStream();
            try {
                return newTextureData(stream, internalFormat, pixelFormat, mipmap, fileSuffix);
            } finally {
                stream.close();
            }
        }
