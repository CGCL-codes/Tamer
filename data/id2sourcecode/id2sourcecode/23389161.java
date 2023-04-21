    public static LTAPMessageDigest digest(DEREncodable rd) throws Exception {
        byte[] dataBytes = LtansUtils.getDERObjectBytes(rd);
        byte[] digestBytes = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM).digest(dataBytes);
        return new LTAPMessageDigest(new DEROctetString(digestBytes), new AlgorithmIdentifier(LtansUtils.defaultDigestOID));
    }
