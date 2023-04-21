    private void exerciseFile(SRP srp) throws IOException {
        File f = new File(pFile);
        if (!f.exists()) {
            if (f.createNewFile()) f.deleteOnExit();
        } else if (!f.isFile()) throw new RuntimeException("File object (./test) exists but is not a file"); else if (!f.canRead() || !f.canWrite()) throw new RuntimeException("File (./test) exists but is not accessible");
        tpasswd = new PasswordFile(pFile, p2File, cFile);
        if (!tpasswd.contains(user)) {
            byte[] testSalt = new byte[10];
            prng.nextBytes(testSalt);
            tpasswd.add(user, password, testSalt, "1");
        } else tpasswd.changePasswd(user, password);
        String[] entry = tpasswd.lookup(user, srp.getAlgorithm());
        BigInteger v = new BigInteger(1, SaslUtil.fromb64(entry[0]));
        byte[] salt = SaslUtil.fromb64(entry[1]);
        String[] mpi = tpasswd.lookupConfig(entry[2]);
        BigInteger N = new BigInteger(1, SaslUtil.fromb64(mpi[0]));
        BigInteger g = new BigInteger(1, SaslUtil.fromb64(mpi[1]));
        KeyPair serverKP = srp.generateServerKeyPair(N, g, v);
        KeyPair clientKP = srp.generateClientKeyPair(N, g);
        SecretKey K1 = srp.generateServerSecretKey(serverKP, ((SRPPublicKey) clientKP.getPublic()).getExponent(), v);
        MessageDigest sha = srp.newDigest();
        sha.update(salt);
        sha.update(srp.userHash(user, password));
        BigInteger x = new BigInteger(1, sha.digest());
        SecretKey K2 = srp.generateClientSecretKey(clientKP, ((SRPPublicKey) serverKP.getPublic()).getExponent(), x);
        if (SaslUtil.areEqual(K1.getEncoded(), K2.getEncoded())) assertTrue(true); else fail();
    }
