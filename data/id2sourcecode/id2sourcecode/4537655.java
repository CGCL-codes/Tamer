    public OutputStream encrypt(OutputStream out) throws Exception {
        if (this.encrypted == null) {
            int choice = JOptionPane.showConfirmDialog(null, Translate.getInstance().get(TranslateKeys.ENCRYPT_DATA_FILE_YES_NO), Translate.getInstance().get(TranslateKeys.ENCRYPT_DATA_FILE_TITLE), JOptionPane.YES_NO_OPTION);
            this.encrypted = new Boolean(JOptionPane.YES_OPTION == choice);
        }
        if (!this.encrypted.booleanValue()) {
            return out;
        }
        if (this.key == null) {
            JPasswordInputDialog jpid = new JPasswordInputDialog(Translate.getInstance().get(TranslateKeys.ENTER_PASSWORD), Translate.getInstance().get(TranslateKeys.ENTER_PASSWORD_TITLE), Translate.getInstance().get(TranslateKeys.PASSWORD), Translate.getInstance().get(TranslateKeys.CONFIRM_PASSWORD), Translate.getInstance().get(TranslateKeys.PASSWORDS_DONT_MATCH), Translate.getInstance().get(TranslateKeys.ERROR), Translate.getInstance().get(TranslateKeys.NO_PASSWORD_ENTERED), Translate.getInstance().get(TranslateKeys.ERROR), Translate.getInstance().get(TranslateKeys.OK), Translate.getInstance().get(TranslateKeys.CANCEL));
            String password = jpid.askForPassword(true, true);
            if (password == null) {
                this.encrypted = false;
                return out;
            }
            this.salt = randomBytes(SALT_LENGTH);
            this.key = PBKDF2.getInstance().passwordToKey(password, this.keySize, KEY_ALGORITHM, this.salt);
        }
        out.write(this.salt);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(this.salt));
        return new CipherOutputStream(out, cipher);
    }
