    protected boolean engineVerify(byte[] sig) throws SignatureException {
        if (verifierKey == null) throw new SignatureException("not initialized for verifying");
        if (sig == null) throw new SignatureException("no signature specified");
        int k = verifierKey.getModulus().bitLength();
        k = (k >>> 3) + ((k & 7) == 0 ? 0 : 1);
        if (sig.length != k) throw new SignatureException("signature is the wrong size (expecting " + k + " bytes, got " + sig.length + ")");
        BigInteger ed = new BigInteger(1, sig);
        byte[] eb = ed.modPow(verifierKey.getPublicExponent(), verifierKey.getModulus()).toByteArray();
        int i = 0;
        if (eb[0] == 0x00) {
            for (i = 1; i < eb.length && eb[i] == 0x00; i++) ;
            if (i == 1) throw new SignatureException("wrong RSA padding");
            i--;
        } else if (eb[0] == 0x01) {
            for (i = 1; i < eb.length && eb[i] != 0x00; i++) if (eb[i] != (byte) 0xFF) throw new IllegalArgumentException("wrong RSA padding");
        } else throw new SignatureException("wrong RSA padding type");
        byte[] d = new byte[eb.length - i - 1];
        System.arraycopy(eb, i + 1, d, 0, eb.length - i - 1);
        DERReader der = new DERReader(d);
        try {
            DERValue val = der.read();
            if (val.getTag() != DER.SEQUENCE) throw new SignatureException("failed to parse DigestInfo");
            val = der.read();
            if (val.getTag() != DER.SEQUENCE) throw new SignatureException("failed to parse DigestAlgorithmIdentifier");
            boolean sequenceIsBer = val.getLength() == 0;
            val = der.read();
            if (val.getTag() != DER.OBJECT_IDENTIFIER) throw new SignatureException("failed to parse object identifier");
            if (!val.getValue().equals(digestAlgorithm)) throw new SignatureException("digest algorithms do not match");
            val = der.read();
            if (val.getTag() != DER.NULL) throw new SignatureException("cannot handle digest parameters");
            if (sequenceIsBer) der.skip(1);
            val = der.read();
            if (val.getTag() != DER.OCTET_STRING) throw new SignatureException("failed to parse Digest");
            return MessageDigest.isEqual(md.digest(), (byte[]) val.getValue());
        } catch (IOException ioe) {
            throw new SignatureException(ioe.toString());
        }
    }
