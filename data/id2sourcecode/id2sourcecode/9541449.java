    private static byte[] decryptKey(byte[] cryptedKey, String password) throws InvalidKeyException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = md.digest(password.getBytes());
            SecretKey secret = new SecretKeySpec(passwordHash, SYMMETRIC_ALG);
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALG);
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return cipher.doFinal(cryptedKey);
        } catch (IllegalBlockSizeException e) {
            throw new Error(e);
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        } catch (NoSuchPaddingException e) {
            throw new InvalidKeyException(e);
        } catch (BadPaddingException e) {
            throw new InvalidKeyException(e);
        }
    }
