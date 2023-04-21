    public void saveKeyPair() throws Exception {
        if (keyfile == null || keyfile.trim().length() == 0) {
            throw new Exception("no 'keyfile' provided");
        }
        SecureRandom rand = createSecureRandom();
        KeyPair kp = generateKeyPair(algorithm, bits, rand);
        boolean sshComFormat = "sshinc".equals(format);
        saveKeyPair(kp, password, keyfile, subject, comment, sshComFormat, rand);
    }
