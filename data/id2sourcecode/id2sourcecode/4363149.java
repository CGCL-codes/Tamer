    public static boolean verifySignature(byte[] message, DSASignature ds, DSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        BigInteger r = ds.getR();
        BigInteger s = ds.getS();
        BigInteger g = dpk.getG();
        BigInteger p = dpk.getP();
        BigInteger q = dpk.getQ();
        BigInteger y = dpk.getY();
        BigInteger zero = BigInteger.ZERO;
        if (log.isEnabled()) {
            log.log(60, "ssh-dss signature: m: " + m.toString(16));
            log.log(60, "ssh-dss signature: r: " + r.toString(16));
            log.log(60, "ssh-dss signature: s: " + s.toString(16));
            log.log(60, "ssh-dss signature: g: " + g.toString(16));
            log.log(60, "ssh-dss signature: p: " + p.toString(16));
            log.log(60, "ssh-dss signature: q: " + q.toString(16));
            log.log(60, "ssh-dss signature: y: " + y.toString(16));
        }
        if (zero.compareTo(r) >= 0 || q.compareTo(r) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(r) >= 0 || q.compareTo(r) <= 0");
            return false;
        }
        if (zero.compareTo(s) >= 0 || q.compareTo(s) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(s) >= 0 || q.compareTo(s) <= 0");
            return false;
        }
        BigInteger w = s.modInverse(q);
        BigInteger u1 = m.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        u1 = g.modPow(u1, p);
        u2 = y.modPow(u2, p);
        BigInteger v = u1.multiply(u2).mod(p).mod(q);
        return v.equals(r);
    }
