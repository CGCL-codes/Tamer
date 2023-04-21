    public void setKeyStore(File basedir) throws EncSupplyException {
        keystore = new File(basedir, KEYSTORE);
        salt = new File(basedir, SALT);
        char[] pw = new char[0];
        try {
            if (!keystore.exists()) {
                pw = getPassword(new PlainMessage("Creating your keystore", "<html>The KeyStore is used to store<br>your keys you use to identify <br> and encrypt messages with</html>"));
                if (pw == null) return;
                KeyPairGenerator kpgen;
                kpgen = KeyPairGenerator.getInstance("RSA");
                kpgen.initialize(KEY_GENERATION_SIZE);
                KeyPair kp = kpgen.generateKeyPair();
                pubkey = kp.getPublic();
                privkey = kp.getPrivate();
                storeKeys(pw);
                startCounter();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new EncSupplyException("Keys weren't able to be created", e, true);
        } finally {
            for (int i = 0; i < pw.length; i++) pw[i] = 0;
        }
    }
