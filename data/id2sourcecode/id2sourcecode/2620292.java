    public static String md5(char[] data) {
        CharBuffer cb = CharBuffer.wrap(data);
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer b = cs.encode(cb);
        md5Digest.reset();
        md5Digest.update(b.array(), b.position(), b.limit());
        Arrays.fill(b.array(), (byte) 0);
        byte[] digest = md5Digest.digest();
        return toHexString(digest);
    }
