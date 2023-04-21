    private void encrypt(InputStream is, OutputStream os, Key wrappingKey) throws EncryptException {
        try {
            SecretKey simmetricKey = this.pkiContext.generateSimmetricKey();
            logger.debug("Using ecryption algorithm: " + this.pkiContext.getSymmetricAlgorithm());
            Cipher cipher = Cipher.getInstance(this.pkiContext.getSymmetricAlgorithm(), "BC");
            cipher.init(Cipher.ENCRYPT_MODE, simmetricKey);
            byte[] buffer = new byte[1024];
            int numBytes = 0;
            byte[] abEncryptedData = null;
            while ((numBytes = is.read(buffer)) > -1) {
                byte[] abTmp = new byte[numBytes];
                System.arraycopy(buffer, 0, abTmp, 0, numBytes);
                abEncryptedData = cipher.update(abTmp);
                if (abEncryptedData != null) {
                    os.write(abEncryptedData);
                }
            }
            abEncryptedData = cipher.doFinal();
            if (abEncryptedData != null) {
                os.write(abEncryptedData);
            }
            logger.debug("Data encryption done.");
            this.setRandomData(cipher.getIV());
            this.setSimetricKeyCiphered(this.wrapKey(simmetricKey, wrappingKey));
        } catch (Exception e) {
            EncryptException ex = new EncryptException(e);
            throw ex;
        }
    }
