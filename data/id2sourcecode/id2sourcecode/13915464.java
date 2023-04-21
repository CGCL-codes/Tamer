    public CryptHjDesc(String name, String medium, String[] attributes, char[] password) throws Exception {
        super(name, medium, attributes, DATA_SUFFIX_CRYPT);
        countWrongKey = 0;
        state = STATE_UNINIT;
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] key = new String(password).getBytes();
        md.update(key);
        byte[] confirmAttribute = md.digest();
        String confAttB64 = Base64.encodeBytes(confirmAttribute);
        Element root = m_document.getRootElement();
        root.setAttribute(ATT_CONFIRM, confAttB64);
        save();
        state = STATE_INIT;
        try {
            Key k = new SecretKeySpec(key, "DES");
            cipherEnc = Cipher.getInstance("DES");
            cipherEnc.init(Cipher.ENCRYPT_MODE, k);
            cipherDec = Cipher.getInstance("DES");
            cipherDec.init(Cipher.DECRYPT_MODE, k);
        } catch (InvalidKeyException e) {
            throw new ITarException(Constants.CREATE_HJ_PASS_CONFORM);
        }
    }
