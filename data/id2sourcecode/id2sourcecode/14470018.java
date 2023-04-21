    public SecureInputStream(InputStream in, MessageDigest md) throws IOException {
        super(in);
        if (md == null) {
            throw new NullPointerException("MessageDigest is null");
        }
        int length = 0;
        for (int i = 0; i < 4; i++) {
            int b = in.read();
            if (b < 0) {
                throw new EOFException("Couldn't read the length of the header");
            }
            length = (length << 8) | (b & 0xFF);
        }
        if (length <= 0 || length >= 512) {
            throw new StreamCorruptedException("Invalid length of the header: " + length);
        }
        byte[] header = new byte[length];
        for (int i = 0; i < header.length; i++) {
            int b = in.read();
            if (b < 0) {
                throw new EOFException("Couldn't read the header");
            }
            header[i] = (byte) (b & 0xFF);
        }
        md.update(header, 0, header.length);
        byte[] actual = md.digest();
        for (int i = 0; i < actual.length; i++) {
            int b = in.read();
            if (b < 0) {
                throw new EOFException("Couldn't read the checksum of length " + actual.length);
            }
            if (actual[i] != (byte) (b & 0xFF)) {
                throw new StreamCorruptedException("Header checksums do not match");
            }
        }
        String algorithm = null;
        int digestLength = 0;
        int blockSize = 0;
        ByteArrayInputStream bias = new ByteArrayInputStream(header);
        DataInputStream dis = new DataInputStream(bias);
        algorithm = dis.readUTF();
        digestLength = dis.readInt();
        blockSize = dis.readInt();
        dis.close();
        if (!algorithm.equals(md.getAlgorithm())) {
            throw new StreamCorruptedException("Expected a MessageDigest of type " + algorithm + " but is " + md.getAlgorithm());
        }
        if (digestLength != md.getDigestLength()) {
            throw new StreamCorruptedException("Expected a MessageDigest with length " + digestLength + " but is " + md.getDigestLength());
        }
        md.reset();
        this.md = md;
        this.buffer = new byte[blockSize];
    }
