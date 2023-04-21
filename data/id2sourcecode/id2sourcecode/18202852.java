    protected SecretKey engineGenerateKey() {
        if (spec == null) {
            throw new IllegalStateException("TlsMasterSecretGenerator must be initialized");
        }
        SecretKey premasterKey = spec.getPremasterSecret();
        byte[] premaster = premasterKey.getEncoded();
        int premasterMajor, premasterMinor;
        if (premasterKey.getAlgorithm().equals("TlsRsaPremasterSecret")) {
            premasterMajor = premaster[0] & 0xff;
            premasterMinor = premaster[1] & 0xff;
        } else {
            premasterMajor = -1;
            premasterMinor = -1;
        }
        try {
            byte[] master;
            byte[] clientRandom = spec.getClientRandom();
            byte[] serverRandom = spec.getServerRandom();
            if (protocolVersion >= 0x0301) {
                byte[] seed = concat(clientRandom, serverRandom);
                master = doPRF(premaster, LABEL_MASTER_SECRET, seed, 48);
            } else {
                master = new byte[48];
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                MessageDigest sha = MessageDigest.getInstance("SHA");
                byte[] tmp = new byte[20];
                for (int i = 0; i < 3; i++) {
                    sha.update(SSL3_CONST[i]);
                    sha.update(premaster);
                    sha.update(clientRandom);
                    sha.update(serverRandom);
                    sha.digest(tmp, 0, 20);
                    md5.update(premaster);
                    md5.update(tmp);
                    md5.digest(master, i << 4, 16);
                }
            }
            return new TlsMasterSecretKey(master, premasterMajor, premasterMinor);
        } catch (NoSuchAlgorithmException e) {
            throw new ProviderException(e);
        } catch (DigestException e) {
            throw new ProviderException(e);
        }
    }
