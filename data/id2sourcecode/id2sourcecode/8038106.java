    public byte[] digestBytes(String name) throws IOException, NoSuchAlgorithmException {
        InputStream src;
        MessageDigest complete;
        src = createInputStream();
        complete = MessageDigest.getInstance(name);
        getIO().getBuffer().digest(src, complete);
        src.close();
        return complete.digest();
    }
