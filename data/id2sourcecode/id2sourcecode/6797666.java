    private OutgoingMessage computeSharedSecret(final IncomingMessage in) throws KeyAgreementException {
        final String I = in.readString();
        final BigInteger A = in.readMPI();
        final Map credentials;
        try {
            final Map userID = new HashMap();
            userID.put(Registry.SASL_USERNAME, I);
            userID.put(SRPRegistry.MD_NAME_FIELD, srp.getAlgorithm());
            credentials = passwordDB.lookup(userID);
        } catch (IOException x) {
            throw new KeyAgreementException("computeSharedSecret()", x);
        }
        final BigInteger s = new BigInteger(1, Util.fromBase64((String) credentials.get(SRPRegistry.SALT_FIELD)));
        final BigInteger v = new BigInteger(1, Util.fromBase64((String) credentials.get(SRPRegistry.USER_VERIFIER_FIELD)));
        final SRPKeyPairGenerator kpg = new SRPKeyPairGenerator();
        final Map attributes = new HashMap();
        if (rnd != null) attributes.put(SRPKeyPairGenerator.SOURCE_OF_RANDOMNESS, rnd);
        attributes.put(SRPKeyPairGenerator.SHARED_MODULUS, N);
        attributes.put(SRPKeyPairGenerator.GENERATOR, g);
        attributes.put(SRPKeyPairGenerator.USER_VERIFIER, v);
        kpg.setup(attributes);
        hostKeyPair = kpg.generate();
        final BigInteger B = ((SRPPublicKey) hostKeyPair.getPublic()).getY();
        final BigInteger u = uValue(A, B);
        final BigInteger b = ((SRPPrivateKey) hostKeyPair.getPrivate()).getX();
        final BigInteger S = A.multiply(v.modPow(u, N)).modPow(b, N);
        final byte[] sBytes = Util.trim(S);
        final IMessageDigest hash = srp.newDigest();
        hash.update(sBytes, 0, sBytes.length);
        K = new BigInteger(1, hash.digest());
        final OutgoingMessage result = new OutgoingMessage();
        result.writeMPI(s);
        result.writeMPI(B);
        complete = true;
        return result;
    }
