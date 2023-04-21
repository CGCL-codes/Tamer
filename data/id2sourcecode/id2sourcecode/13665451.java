    public static byte[] createPassword(byte password[], byte oldPassword[]) {
        byte result[] = null;
        try {
            MessageDigest sha = MessageDigest.getInstance(ALGORITHMS_DIGEST);
            SecureRandom random = null;
            if (oldPassword == null) random = SecureRandom.getInstance(ALGORITHMS_RANDOM);
            if (oldPassword != null) random = new SecureRandom(oldPassword);
            byte salt[] = random.generateSeed(SALT_MIN_LEN + password.length);
            sha.reset();
            sha.update(password);
            sha.update(salt);
            byte encr[] = sha.digest(salt);
            result = new byte[encr.length + salt.length];
            System.arraycopy(encr, 0, result, 0, encr.length);
            System.arraycopy(salt, 0, result, encr.length, salt.length);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }
