    @Override
    public byte[] getMD5() throws S3ClientException {
        InputStream input;
        MessageDigest md;
        byte data[];
        int nbytes;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException nsa) {
            throw new RuntimeException(nsa);
        }
        input = getInputStream();
        data = new byte[1024];
        try {
            while ((nbytes = input.read(data)) > 0) {
                md.update(data, 0, nbytes);
            }
        } catch (IOException ioe) {
            throw new S3ClientException("Failure reading input file: " + ioe, ioe);
        }
        return md.digest();
    }
