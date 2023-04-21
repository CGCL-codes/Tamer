    public void writeDataManager(DataManager dm, String filename, char[] password) throws IOException {
        OutputStream out = null;
        if (password != null) {
            Cipher cipher = CipherFactory.createCipher(Globals.getENCRYPTION_CIPHER(), Globals.getENCRYPTION_MODE(), Globals.getENCRYPTION_PADDING());
            SecretKey key = KeyManager.createKey(password);
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key, KeyManager.getParameterSpec());
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
            out = new BufferedOutputStream(new CipherOutputStream(new FileOutputStream(filename), cipher));
        } else {
            out = new BufferedOutputStream(new FileOutputStream(filename));
        }
        this.mXmlStream.streamDataManagerOut(dm, out);
        out.flush();
        out.close();
    }
