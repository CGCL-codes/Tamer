    private OutgoingMessage computeSharedSecret(final IncomingMessage in) throws KeyAgreementException {
        final BigInteger s = in.readMPI();
        final BigInteger B = in.readMPI();
        final BigInteger A = ((SRPPublicKey) userKeyPair.getPublic()).getY();
        final BigInteger u = uValue(A, B);
        final BigInteger x;
        try {
            x = new BigInteger(1, srp.computeX(Util.trim(s), I, p));
        } catch (Exception e) {
            throw new KeyAgreementException("computeSharedSecret()", e);
        }
        final BigInteger a = ((SRPPrivateKey) userKeyPair.getPrivate()).getX();
        final BigInteger S = B.subtract(THREE.multiply(g.modPow(x, N))).modPow(a.add(u.multiply(x)), N);
        final byte[] sBytes = Util.trim(S);
        final IMessageDigest hash = srp.newDigest();
        hash.update(sBytes, 0, sBytes.length);
        K = new BigInteger(1, hash.digest());
        complete = true;
        return null;
    }
