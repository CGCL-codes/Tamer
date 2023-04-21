    public static final byte[] Encode(PublicKey pubKey) throws IOException {
        DerValue algAndKey = new DerValue(pubKey.getEncoded());
        if (algAndKey.tag != DerValue.tag_Sequence) throw new IOException("PublicKey value is not a valid " + "X.509 public key"); else {
            AlgorithmId algid = AlgorithmId.parse(algAndKey.data.getDerValue());
            byte[] key = algAndKey.data.getUnalignedBitString().toByteArray();
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e3) {
                throw new IOException("SHA1 not supported");
            }
            md.update(key);
            return md.digest();
        }
    }
