    public static byte[] encryptKey(byte[] plainKey, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = md.digest(password.getBytes());
            SecretKey secret = new SecretKeySpec(passwordHash, SYMMETRIC_ALG);
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(plainKey);
        } catch (IllegalBlockSizeException e) {
            throw new Error(e);
        } catch (BadPaddingException e) {
            throw new Error(e);
        } catch (InvalidKeyException e) {
            throw new Error(e);
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        } catch (NoSuchPaddingException e) {
            throw new Error(e);
        }
    }
