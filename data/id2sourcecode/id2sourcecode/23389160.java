    public DEROctetString digestRawData(DEREncodable rd) throws Exception {
        byte[] dataBytes = LtansUtils.getDERObjectBytes(rd);
        byte[] digestBytes = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM).digest(dataBytes);
        return new DEROctetString(digestBytes);
    }
