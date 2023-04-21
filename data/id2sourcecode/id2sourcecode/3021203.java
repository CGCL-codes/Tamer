    public byte[] readBytes() throws IOException {
        int tag = is.read();
        if (tag == 'N') return null;
        if (tag != 'B') throw expect("bytes", tag);
        int b16 = is.read();
        int b8 = is.read();
        int len = (b16 << 8) + b8;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < len; i++) bos.write(is.read());
        return bos.toByteArray();
    }
