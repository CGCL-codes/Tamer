    public static boolean verify(final byte[] data, final SigShare[] sigs, final int k, final int l, final BigInteger n, final BigInteger e) throws ThresholdSigException {
        final boolean[] haveSig = new boolean[l];
        for (int i = 0; i < k; i++) {
            if (sigs[i] == null) {
                throw new ThresholdSigException("Null signature");
            }
            if (haveSig[sigs[i].getId() - 1]) {
                throw new ThresholdSigException("Duplicate signature: " + sigs[i].getId());
            }
            haveSig[sigs[i].getId() - 1] = true;
        }
        final BigInteger x = (new BigInteger(data)).mod(n);
        final BigInteger delta = SigShare.factorial(l);
        if (CHECKVERIFIER) {
            final BigInteger FOUR = BigInteger.valueOf(4l);
            final BigInteger TWO = BigInteger.valueOf(2l);
            final BigInteger xtilde = x.modPow(FOUR.multiply(delta), n);
            try {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                for (int i = 0; i < k; i++) {
                    md.reset();
                    final Verifier ver = sigs[i].getSigVerifier();
                    final BigInteger v = ver.getGroupVerifier();
                    final BigInteger vi = ver.getShareVerifier();
                    md.update(v.toByteArray());
                    md.update(xtilde.toByteArray());
                    md.update(vi.toByteArray());
                    final BigInteger xi = sigs[i].getSig();
                    md.update(xi.modPow(TWO, n).toByteArray());
                    final BigInteger vz = v.modPow(ver.getZ(), n);
                    final BigInteger vinegc = vi.modPow(ver.getC(), n).modInverse(n);
                    md.update(vz.multiply(vinegc).mod(n).toByteArray());
                    final BigInteger xtildez = xtilde.modPow(ver.getZ(), n);
                    final BigInteger xineg2c = xi.modPow(ver.getC(), n).modInverse(n);
                    md.update(xineg2c.multiply(xtildez).mod(n).toByteArray());
                    final BigInteger result = new BigInteger(md.digest()).mod(n);
                    if (!result.equals(ver.getC())) {
                        debug("Share verifier is not OK");
                        return false;
                    }
                }
            } catch (final java.security.NoSuchAlgorithmException ex) {
                debug("Provider could not locate SHA message digest .");
                ex.printStackTrace();
            }
        }
        BigInteger w = BigInteger.valueOf(1l);
        for (int i = 0; i < k; i++) {
            w = w.multiply(sigs[i].getSig().modPow(SigShare.lambda(sigs[i].getId(), sigs, delta), n));
        }
        final BigInteger eprime = delta.multiply(delta).shiftLeft(2);
        w = w.mod(n);
        final BigInteger xeprime = x.modPow(eprime, n);
        final BigInteger we = w.modPow(e, n);
        return (xeprime.compareTo(we) == 0);
    }
