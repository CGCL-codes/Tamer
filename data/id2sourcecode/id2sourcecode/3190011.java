    protected byte[] engineSign() throws SignatureException {
        if (signerKey == null) throw new SignatureException("not initialized for signing");
        ArrayList digestAlg = new ArrayList(2);
        digestAlg.add(new DERValue(DER.OBJECT_IDENTIFIER, digestAlgorithm));
        digestAlg.add(new DERValue(DER.NULL, null));
        ArrayList digestInfo = new ArrayList(2);
        digestInfo.add(new DERValue(DER.SEQUENCE, digestAlg));
        digestInfo.add(new DERValue(DER.OCTET_STRING, md.digest()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DERWriter.write(out, new DERValue(DER.SEQUENCE, digestInfo));
        } catch (IOException ioe) {
            throw new SignatureException(ioe.toString());
        }
        byte[] buf = out.toByteArray();
        md.reset();
        int k = signerKey.getModulus().bitLength();
        k = (k >>> 3) + ((k & 7) == 0 ? 0 : 1);
        if (buf.length < k - 3) {
            throw new SignatureException("RSA modulus too small");
        }
        byte[] d = new byte[k];
        d[1] = 0x01;
        for (int i = 2; i < k - buf.length - 1; i++) d[i] = (byte) 0xFF;
        System.arraycopy(buf, 0, d, k - buf.length, buf.length);
        BigInteger eb = new BigInteger(d);
        byte[] ed = eb.modPow(signerKey.getPrivateExponent(), signerKey.getModulus()).toByteArray();
        if (ed.length < k) {
            byte[] b = new byte[k];
            System.arraycopy(eb, 0, b, k - ed.length, ed.length);
            ed = b;
        } else if (ed.length > k) {
            if (ed.length != k + 1) {
                throw new SignatureException("modPow result is larger than the modulus");
            }
            byte[] b = new byte[k];
            System.arraycopy(ed, 1, b, 0, k);
            ed = b;
        }
        return ed;
    }
