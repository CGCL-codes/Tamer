    protected byte[] engineSign() throws SignatureException {
        BigInteger r = null;
        BigInteger s = null;
        BigInteger k = null;
        BigInteger p, q, g, x;
        BigInteger digestBI;
        byte randomBytes[];
        byte rBytes[], sBytes[], signature[];
        int n, n1, n2;
        DSAParams params;
        if (appRandom == null) {
            appRandom = new SecureRandom();
        }
        params = dsaKey.getParams();
        p = params.getP();
        q = params.getQ();
        g = params.getG();
        x = ((DSAPrivateKey) dsaKey).getX();
        digestBI = new BigInteger(1, msgDigest.digest());
        randomBytes = new byte[20];
        for (; ; ) {
            appRandom.nextBytes(randomBytes);
            k = new BigInteger(1, randomBytes);
            if (k.compareTo(q) != -1) {
                continue;
            }
            r = g.modPow(k, p).mod(q);
            if (r.signum() == 0) {
                continue;
            }
            s = k.modInverse(q).multiply(digestBI.add(x.multiply(r)).mod(q)).mod(q);
            if (s.signum() != 0) {
                break;
            }
        }
        rBytes = r.toByteArray();
        n1 = rBytes.length;
        if ((rBytes[0] & 0x80) != 0) {
            n1++;
        }
        sBytes = s.toByteArray();
        n2 = sBytes.length;
        if ((sBytes[0] & 0x80) != 0) {
            n2++;
        }
        signature = new byte[6 + n1 + n2];
        signature[0] = (byte) 0x30;
        signature[1] = (byte) (4 + n1 + n2);
        signature[2] = (byte) 0x02;
        signature[3] = (byte) n1;
        signature[4 + n1] = (byte) 0x02;
        signature[5 + n1] = (byte) n2;
        if (n1 == rBytes.length) {
            n = 4;
        } else {
            n = 5;
        }
        System.arraycopy(rBytes, 0, signature, n, rBytes.length);
        if (n2 == sBytes.length) {
            n = 6 + n1;
        } else {
            n = 7 + n1;
        }
        System.arraycopy(sBytes, 0, signature, n, sBytes.length);
        return signature;
    }
