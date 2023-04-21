    public boolean verify(byte[] cert) throws NoSuchAlgorithmException {
        MessageDigest dig;
        String alg;
        byte[] buf;
        alg = hashAlgorithm_.getAlgorithmName();
        if (alg == null) {
            throw new NoSuchAlgorithmException(hashAlgorithm_.getAlgorithmOID().toString());
        }
        dig = MessageDigest.getInstance(alg);
        buf = dig.digest(cert);
        return Arrays.equals(buf, certificateHash_.getByteArray());
    }
