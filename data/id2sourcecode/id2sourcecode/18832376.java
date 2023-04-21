    public DecoderInputStream(DecoderInterface decoder) {
        this.decoder = decoder;
        this.buffer = new CircularBuffer<Byte>(new Byte[2 * decoder.getSampleRate() * (decoder.getSampleSize() / 8) * decoder.getChannels()]);
    }
