    UpSample2InputStream(float[] f, AudioInputStream in) {
        this.f = f;
        this.in = in;
        fl2 = f.length / 2;
        fl4 = f.length / 4;
        si0 = -((f.length - 1) / 2);
        si1 = -((f.length - 2) / 2);
        presigSize = fl4;
        sigp = 0;
        presig = presigSize;
        audioFormat = in.getFormat();
        channels = audioFormat.getChannels();
        byteBuffer = ByteBuffer.allocate(SIZE);
        byteBuffer.order(audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        sb = byteBuffer.asShortBuffer();
        sigb = new float[f.length * channels];
        for (int i = 0; i < sigb.length; i++) {
            sigb[i] = 0f;
        }
    }
