    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws NoSuchAlgorithmException {
        LOG.debug("preSign");
        String toBeSigned = "to be signed";
        String digestAlgo = "SHA-1";
        MessageDigest messageDigest = MessageDigest.getInstance(digestAlgo);
        byte[] digestValue = messageDigest.digest(toBeSigned.getBytes());
        String description = "Test Document";
        return new DigestInfo(digestValue, digestAlgo, description);
    }
