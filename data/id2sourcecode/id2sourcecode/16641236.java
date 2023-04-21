    private byte[] sendEvidence(byte[] input) throws SaslException {
        cat.debug("==> sendEvidence()");
        cat.debug("challenge: " + SaslUtil.dumpString(input));
        InputBuffer frameIn = new InputBuffer(input);
        BigInteger B;
        try {
            B = frameIn.getMPI();
            cat.debug("Got B (server's ephermeral public key): " + SaslUtil.dump(B));
        } catch (IOException x) {
            if (x instanceof SaslException) throw (SaslException) x;
            throw new SaslException("sendEvidence()", x);
        }
        MessageDigest ctxt = srp.newDigest();
        ctxt.update(salt);
        ctxt.update(srp.userHash(U, new String(password)));
        BigInteger _x = new BigInteger(1, ctxt.digest());
        cat.debug("x: " + SaslUtil.dump(_x));
        K = srp.generateClientSecretKey(kp, B, _x);
        byte[] k = K.getEncoded();
        M1 = srp.generateClientEvidence(N, g, U, salt, A, B, k, L);
        cat.debug("Encoding M1 (client's evidence): " + SaslUtil.dumpString(M1));
        OutputBuffer frameOut = new OutputBuffer();
        try {
            frameOut.setOS(M1);
        } catch (IOException x) {
            if (x instanceof SaslException) throw (SaslException) x;
            throw new SaslException("sendEvidence()", x);
        }
        byte[] result = frameOut.encode();
        if (chosenConfidentialityAlgorithm != null) setupSecurityServices();
        cat.debug("<== sendEvidence() --> " + SaslUtil.dumpString(result));
        cat.info("C: " + Base64.encode(result));
        return result;
    }
