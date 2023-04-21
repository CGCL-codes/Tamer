    public static boolean verify(byte password[], byte dbpassword[]) {
        boolean result = false;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(ALGORITHMS_DIGEST);
            sha.reset();
            int startAt = dbpassword.length - (SALT_MIN_LEN + password.length);
            if (startAt > 0) {
                byte salt[] = new byte[SALT_MIN_LEN + password.length];
                byte enc[] = new byte[startAt];
                System.arraycopy(dbpassword, startAt, salt, 0, salt.length);
                System.arraycopy(dbpassword, 0, enc, 0, enc.length);
                sha.update(password);
                sha.update(salt);
                byte toBytePassword[] = sha.digest(salt);
                result = MessageDigest.isEqual(toBytePassword, enc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
