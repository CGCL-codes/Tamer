    public Object readObject(Class expectedClass) throws IOException {
        int tag = is.read();
        switch(tag) {
            case 'N':
                return null;
            case 'T':
                return new Boolean(true);
            case 'F':
                return new Boolean(false);
            case 'I':
                {
                    int b32 = is.read();
                    int b24 = is.read();
                    int b16 = is.read();
                    int b8 = is.read();
                    return new Integer((b32 << 24) + (b24 << 16) + (b16 << 8) + b8);
                }
            case 'L':
                {
                    long b64 = is.read();
                    long b56 = is.read();
                    long b48 = is.read();
                    long b40 = is.read();
                    long b32 = is.read();
                    long b24 = is.read();
                    long b16 = is.read();
                    long b8 = is.read();
                    return new Long((b64 << 56) + (b56 << 48) + (b48 << 40) + (b40 << 32) + (b32 << 24) + (b24 << 16) + (b16 << 8) + b8);
                }
            case 'd':
                {
                    long b64 = is.read();
                    long b56 = is.read();
                    long b48 = is.read();
                    long b40 = is.read();
                    long b32 = is.read();
                    long b24 = is.read();
                    long b16 = is.read();
                    long b8 = is.read();
                    return new Date((b64 << 56) + (b56 << 48) + (b48 << 40) + (b40 << 32) + (b32 << 24) + (b24 << 16) + (b16 << 8) + b8);
                }
            case 'S':
            case 'X':
                {
                    int b16 = is.read();
                    int b8 = is.read();
                    int len = (b16 << 8) + b8;
                    return readStringImpl(len);
                }
            case 'B':
                {
                    if (tag != 'B') throw expect("bytes", tag);
                    int b16 = is.read();
                    int b8 = is.read();
                    int len = (b16 << 8) + b8;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    for (int i = 0; i < len; i++) bos.write(is.read());
                    return bos.toByteArray();
                }
            default:
                throw new IOException("unknown code:" + (char) tag);
        }
    }
