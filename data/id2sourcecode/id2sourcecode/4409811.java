    public byte[] getByts(File file) throws IOException {
        DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(file), digest);
        while (digestInputStream.read() != -1) {
        }
        byte[] bytes = digestInputStream.getMessageDigest().digest();
        digestInputStream.close();
        return bytes;
    }
