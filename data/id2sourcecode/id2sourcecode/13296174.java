    private boolean checkSignature(byte[] sigBytes, int offset, int length) throws SignatureException {
        BigInteger r, s, w;
        BigInteger u1, u2, v;
        BigInteger p, q, g, y;
        DSAParams params;
        int n1, n2;
        byte bytes[];
        byte digest[];
        try {
            byte dummy;
            n1 = sigBytes[offset + 3];
            n2 = sigBytes[offset + n1 + 5];
            if (sigBytes[offset + 0] != 0x30 || sigBytes[offset + 2] != 2 || sigBytes[offset + n1 + 4] != 2 || sigBytes[offset + 1] != (n1 + n2 + 4) || n1 > 21 || n2 > 21 || (length != 0 && (sigBytes[offset + 1] + 2) > length)) {
                throw new SignatureException(Messages.getString("security.16F"));
            }
            dummy = sigBytes[5 + n1 + n2];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SignatureException(Messages.getString("security.170"));
        }
        digest = msgDigest.digest();
        bytes = new byte[n1];
        System.arraycopy(sigBytes, offset + 4, bytes, 0, n1);
        r = new BigInteger(bytes);
        bytes = new byte[n2];
        System.arraycopy(sigBytes, offset + 6 + n1, bytes, 0, n2);
        s = new BigInteger(bytes);
        params = dsaKey.getParams();
        p = params.getP();
        q = params.getQ();
        g = params.getG();
        y = ((DSAPublicKey) dsaKey).getY();
        if (r.signum() != 1 || r.compareTo(q) != -1 || s.signum() != 1 || s.compareTo(q) != -1) {
            return false;
        }
        w = s.modInverse(q);
        u1 = (new BigInteger(1, digest)).multiply(w).mod(q);
        u2 = r.multiply(w).mod(q);
        v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
        if (v.compareTo(r) != 0) {
            return false;
        }
        return true;
    }
