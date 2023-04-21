    public static synchronized byte[] encrypt(byte[] input, Key key, boolean encrypt) {
        int encodinglength = -10;
        try {
            Cipher cip = Cipher.getInstance(ALGORITHM);
            if (encrypt) cip.init(javax.crypto.Cipher.ENCRYPT_MODE, key); else cip.init(javax.crypto.Cipher.DECRYPT_MODE, key);
            ByteArrayOutputStream bla = new ByteArrayOutputStream();
            encodinglength = cip.getOutputSize(2);
            if (encrypt) encodinglength = cip.getOutputSize(2) - 20;
            int i = 0;
            boolean finished = false;
            do {
                bla.write(cip.doFinal(ByteArray.copyfromto(input, i * encodinglength, (i + 1) * encodinglength)));
                i++;
                if (input.length <= i * encodinglength) finished = true;
            } while (!finished);
            byte[] encrypted = bla.toByteArray();
            return encrypted;
        } catch (InvalidKeyException e) {
            log.warn("", e);
        } catch (IOException e) {
            log.warn("", e);
        } catch (NoSuchAlgorithmException e) {
            log.warn("", e);
        } catch (NoSuchPaddingException e) {
            log.warn("", e);
        } catch (IllegalBlockSizeException e) {
            log.warn("", e);
        } catch (BadPaddingException e) {
            log.warn("", e);
        }
        return null;
    }
