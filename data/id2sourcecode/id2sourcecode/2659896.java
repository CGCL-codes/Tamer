    public GLReadBufferUtil(boolean alpha, boolean write2Texture) {
        components = alpha ? 4 : 3;
        alignment = alpha ? 4 : 1;
        readTexture = write2Texture ? new Texture(GL.GL_TEXTURE_2D) : null;
        psm = new GLPixelStorageModes();
    }
