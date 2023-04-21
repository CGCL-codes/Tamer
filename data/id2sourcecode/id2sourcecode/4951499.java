    public byte[] getImplicitName() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest;
        InputStream in;
        byte[] buf;
        int n;
        in = getSF(AgentStructure.OWNER);
        try {
            buf = new byte[BUFFER_SIZE];
            digest = MessageDigest.getInstance(AgentStructure.IMPLICIT_NAME_ALG);
            while ((n = in.read(buf)) > 0) {
                digest.update(buf, 0, n);
            }
            return digest.digest();
        } finally {
            in.close();
        }
    }
