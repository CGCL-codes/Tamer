    private static String getMD5Hash(String password) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(DIGEST_ALGORITHM);
        String paddedPassword = password;
        char[] old_password = password.toCharArray();
        if (old_password.length < MD5_HASH_LENGTH) {
            char[] new_password = new char[MD5_HASH_LENGTH];
            for (int i = 0; i < old_password.length; i++) new_password[i] = old_password[i];
            for (int i = old_password.length; i < new_password.length; i++) new_password[i] = PAD_CHARACTER;
            paddedPassword = new String(new_password);
        }
        byte[] buffer = new byte[MD5_HASH_LENGTH];
        byte[] valBytes = paddedPassword.getBytes();
        for (int i = 0; i < valBytes.length; i++) buffer[i] = valBytes[i];
        md5.reset();
        md5.update(buffer, 0, MD5_HASH_LENGTH);
        byte[] hash_raw = new byte[MD5_HASH_LENGTH];
        hash_raw = md5.digest();
        return Base64.encode(hash_raw);
    }
