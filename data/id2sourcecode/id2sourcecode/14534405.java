    private static Cipher createOldFormatCipher(String password, int mode) throws Exception {
        try {
            byte[] salt = password.getBytes();
            MessageDigest hasher = MessageDigest.getInstance("SHA-512");
            for (int i = 0; i < CryptnosApplication.SALT_ITERATION_COUNT; i++) salt = hasher.digest(salt);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, CryptnosApplication.KEY_ITERATION_COUNT, CryptnosApplication.KEY_LENGTH);
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance(CryptnosApplication.KEY_FACTORY);
            SecretKey key = keyFac.generateSecret(pbeKeySpec);
            AlgorithmParameterSpec aps = new PBEParameterSpec(salt, CryptnosApplication.KEY_ITERATION_COUNT);
            Cipher cipher = Cipher.getInstance(CryptnosApplication.KEY_FACTORY);
            switch(mode) {
                case Cipher.ENCRYPT_MODE:
                    cipher.init(Cipher.ENCRYPT_MODE, key, aps);
                    break;
                case Cipher.DECRYPT_MODE:
                    cipher.init(Cipher.DECRYPT_MODE, key, aps);
                    break;
                default:
                    throw new Exception("Invalid cipher mode");
            }
            return cipher;
        } catch (Exception e) {
            throw e;
        }
    }
