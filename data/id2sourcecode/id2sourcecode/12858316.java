    public DataBuffer(IMpeg4 mpeg4, InputStream is, int movieLength, int maxBufferSize, boolean encryptedStream) {
        LSystem.gc();
        this.mpeg4 = mpeg4;
        this.is = is;
        this.encryption_index = this.readed = this.writed = this.wait_for_data = 0;
        this.movieLength = movieLength;
        this.initMaxBufferSize = this.maxBufferSize = maxBufferSize;
        this.minBufferSize = maxBufferSize / 5;
        this.encryptedStream = encryptedStream;
        this.buffering = true;
        this.reBuffering = false;
        this.movieData = new byte[movieLength];
        if (encryptedStream) {
            try {
                encryption_key = (byte[]) Class.forName("mediaframe.mpeg4.LicenseManager").getMethod("getEncryptionKey", new Class[0]).invoke(null, new Object[0]);
            } catch (Throwable ex) {
                this.encryptedStream = false;
            }
        }
        bufferThread = new Thread(this, "Buffer Thread");
        bufferThread.start();
    }
