    private String[] getResponsesInternal(String account, String password, String seed) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = md5.digest(password.getBytes());
        byte[] password_hash = Base64.toYmsgBase64(result);
        md5 = MessageDigest.getInstance("MD5");
        byte[] crypt_result = Crypt.crypt(password, "$1$_2S43d5f$");
        result = md5.digest(crypt_result);
        byte[] crypt_hash = Base64.toYmsgBase64(result);
        int sv = seed.charAt(15);
        sv = sv % 8;
        String hash_string_p = null;
        String hash_string_c = null;
        char checksum;
        switch(sv) {
            case 1:
            case 6:
                checksum = seed.charAt(seed.charAt(9) % 16);
                hash_string_p = checksum + account + seed + new String(password_hash);
                hash_string_c = checksum + account + seed + new String(crypt_hash);
                break;
            case 2:
            case 7:
                checksum = seed.charAt(seed.charAt(15) % 16);
                hash_string_p = checksum + seed + new String(password_hash) + account;
                hash_string_c = checksum + seed + new String(crypt_hash) + account;
                break;
            case 3:
                checksum = seed.charAt(seed.charAt(1) % 16);
                hash_string_p = checksum + account + new String(password_hash) + seed;
                hash_string_c = checksum + account + new String(crypt_hash) + seed;
                break;
            case 4:
                checksum = seed.charAt(seed.charAt(3) % 16);
                hash_string_p = checksum + new String(password_hash) + seed + account;
                hash_string_c = checksum + new String(crypt_hash) + seed + account;
                break;
            case 0:
            case 5:
                checksum = seed.charAt(seed.charAt(7) % 16);
                hash_string_p = checksum + new String(password_hash) + account + seed;
                hash_string_c = checksum + new String(crypt_hash) + account + seed;
                break;
        }
        md5 = MessageDigest.getInstance("MD5");
        result = md5.digest(hash_string_p.getBytes());
        byte[] result6 = Base64.toYmsgBase64(result);
        md5 = MessageDigest.getInstance("MD5");
        result = md5.digest(hash_string_c.getBytes());
        byte[] result96 = Base64.toYmsgBase64(result);
        return new String[] { new String(result6), new String(result96) };
    }
