    public boolean engineVerify(byte[] sigBytes) throws SignatureException {
        try {
            DERReader in = new DERReader(sigBytes);
            DERValue val = in.read();
            if (!val.isConstructed()) throw new SignatureException("badly formed signature");
            BigInteger r = (BigInteger) in.read().getValue();
            BigInteger s = (BigInteger) in.read().getValue();
            BigInteger g = publicKey.getParams().getG();
            BigInteger p = publicKey.getParams().getP();
            BigInteger q = publicKey.getParams().getQ();
            BigInteger y = publicKey.getY();
            BigInteger w = s.modInverse(q);
            byte bytes[] = digest.digest();
            BigInteger sha = new BigInteger(1, bytes);
            BigInteger u1 = w.multiply(sha).mod(q);
            BigInteger u2 = r.multiply(w).mod(q);
            BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
            if (v.equals(r)) return true; else return false;
        } catch (IOException ioe) {
            SignatureException se = new SignatureException("badly formed signature");
            se.initCause(ioe);
            throw se;
        }
    }
