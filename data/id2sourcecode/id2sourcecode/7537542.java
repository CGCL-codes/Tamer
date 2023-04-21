    public byte[] digestBytes(String name) throws IOException, NoSuchAlgorithmException {
        InputStream src;
        MessageDigest digest;
        Buffer buffer;
        src = createInputStream();
        digest = MessageDigest.getInstance(name);
        synchronized (digest) {
            buffer = getWorld().getBuffer();
            synchronized (buffer) {
                buffer.digest(src, digest);
            }
            src.close();
            return digest.digest();
        }
    }
