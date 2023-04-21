    public int read(byte[] buff, int size) throws Exception {
        if (size <= 0) {
            return 0;
        }
        int firstchunk = this.pos / this.chlen;
        int offset = this.pos - firstchunk * this.chlen;
        int lastchunk = (this.pos + size) / this.chlen;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        for (int i = firstchunk; i <= lastchunk; i++) {
            byteStream.write(this._readchunk(i));
        }
        byte[] buf = byteStream.toByteArray();
        for (int i = 0; i < size; i++) {
            buff[i] = buf[offset + i];
        }
        return 0;
    }
