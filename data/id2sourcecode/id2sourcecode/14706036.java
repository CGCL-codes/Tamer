    private String digest(String msg, MessageDigest digest) {
        digest.reset();
        byte[] bytes = msg.getBytes();
        byte[] out = digest.digest(bytes);
        BASE64Encoder enc = new BASE64Encoder();
        return enc.encode(out);
    }
