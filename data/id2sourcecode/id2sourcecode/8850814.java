    public OutputStream createFilterStream(OutputStream sameDirection, OutputStream otherDirection, Parameters parameters) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            IvParameterSpec ivSpec = null;
            byte[] iv = expression.getSecondaryValue();
            if (iv != null) {
                if (expression.getMarkedSecondaryBytes() == null) {
                    ivSpec = new IvParameterSpec(iv);
                } else {
                    byte[] tempIV = expression.getRandomSecondaryValue(new SecureRandom());
                    sameDirection.write(tempIV);
                    ivSpec = new IvParameterSpec(tempIV);
                }
            }
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            return new CipherOutputStream(sameDirection, cipher);
        } catch (GeneralSecurityException ex) {
            IOException ex2 = new IOException("Unable to initialize cipher");
            ex2.initCause(ex);
            throw ex2;
        }
    }
