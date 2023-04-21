    public void createKeyFile(String password) throws NcrException {
        PBEParameterSpec parameterSpec;
        SecretKey key;
        OutputStream out = null;
        try {
            Cipher ecipher = Cipher.getInstance(NcrConstants.PBE_ALGO);
            parameterSpec = new PBEParameterSpec(iv, 100);
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(NcrConstants.PBE_ALGO);
            key = keyFactory.generateSecret(keySpec);
            ecipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            out = new FileOutputStream(objFile);
            out = new CipherOutputStream(out, ecipher);
            keyStream = new ObjectOutputStream(out);
            KeyGenerator keygen = KeyGenerator.getInstance(NcrConstants.FILE_ALGO);
            fileKey = keygen.generateKey();
            keyStream.writeObject(fileKey);
            keyStream.flush();
            this.writeKeyData(keyStream, "version", NcrConstants.VERSION);
            this.writeKeyData(keyStream, "pbeAlgo", NcrConstants.PBE_ALGO);
            this.writeKeyData(keyStream, "fileAlgo", NcrConstants.FILE_ALGO);
        } catch (Exception ex) {
            Helper.close(out);
            throw new NcrException("Error while creating key file.");
        }
    }
