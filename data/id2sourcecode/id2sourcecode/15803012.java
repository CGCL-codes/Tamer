    public String generatePpiForThisRP(String infoCardPpi, String rpData) throws TokenIssuanceException {
        byte[] keyBytes = new byte[16];
        byte[] b;
        try {
            b = infoCardPpi.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TokenIssuanceException(e);
        }
        int len = b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(rpData.getBytes("UTF-8"));
            MessageDigest mdAlgorithm = MessageDigest.getInstance("SHA-1");
            mdAlgorithm.update(encrypted);
            byte[] digest = mdAlgorithm.digest();
            return Base64.encodeBytesNoBreaks(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new TokenIssuanceException(e);
        } catch (NoSuchPaddingException e) {
            throw new TokenIssuanceException(e);
        } catch (InvalidKeyException e) {
            throw new TokenIssuanceException(e);
        } catch (IllegalBlockSizeException e) {
            throw new TokenIssuanceException(e);
        } catch (BadPaddingException e) {
            throw new TokenIssuanceException(e);
        } catch (UnsupportedEncodingException e) {
            throw new TokenIssuanceException(e);
        }
    }
