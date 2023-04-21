    public byte[] getImplicitName() throws IOException, NoSuchAlgorithmException {
        MessageDigest md;
        InputStream in;
        byte[] buf;
        int n;
        if (ext_ == null) {
            throw new IllegalStateException("The static part of the agent must be signed first!");
        }
        md = MessageDigest.getInstance(AgentStructure.IMPLICIT_NAME_ALG);
        buf = new byte[BUF_SIZE];
        in = struct_.getInputStream(AgentStructure.META_INF + AgentStructure.OWNER + ext_);
        if (in == null) {
            throw new IllegalStateException("Cannot find the OWNER signature!");
        }
        try {
            while ((n = in.read(buf)) > 0) {
                md.update(buf, 0, n);
            }
            return md.digest();
        } finally {
            in.close();
        }
    }
