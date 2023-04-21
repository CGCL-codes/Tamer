    public SecureOutputStream(OutputStream out, MessageDigest md, int blockSize) throws IOException {
        super(out);
        if (md == null) {
            throw new NullPointerException("MessageDigest is null");
        }
        int digestLength = md.getDigestLength();
        if (digestLength == 0) {
            throw new IllegalArgumentException("Digest length is undefined");
        }
        if (blockSize <= 0) {
            throw new IllegalArgumentException("Illegal block size: " + blockSize);
        }
        if (blockSize <= digestLength) {
            throw new IllegalArgumentException("Block size must be greater than digest length: " + blockSize + " <= " + digestLength);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(md.getAlgorithm());
        dos.writeInt(md.getDigestLength());
        dos.writeInt(blockSize);
        dos.close();
        byte[] header = baos.toByteArray();
        out.write((header.length >> 24) & 0xFF);
        out.write((header.length >> 16) & 0xFF);
        out.write((header.length >> 8) & 0xFF);
        out.write((header.length) & 0xFF);
        out.write(header, 0, header.length);
        md.update(header, 0, header.length);
        byte[] digest = md.digest();
        out.write(digest, 0, digest.length);
        md.reset();
        this.md = md;
        this.dataSize = blockSize - digestLength;
    }
