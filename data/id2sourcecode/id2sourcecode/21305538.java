    @Override
    public OutputStream getOutputStream(final String path) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException nsae) {
            throw new IOException();
        }
        DigestOutputStream os = new DigestOutputStream(super.getOutputStream(path), digest) {

            @Override
            public void close() throws IOException {
                try {
                    byte[] digest = getMessageDigest().digest();
                    SignedZipFileOutput.this.entries.add(new Entry(path, digest));
                } finally {
                    super.close();
                }
            }
        };
        return os;
    }
