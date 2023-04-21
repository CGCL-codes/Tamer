    public ReadBufferBase(GLDrawable externalRead, boolean write2Texture) {
        this.externalRead = externalRead;
        this.readBufferUtil = new GLReadBufferUtil(false, write2Texture);
    }
