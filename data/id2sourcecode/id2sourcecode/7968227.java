    public boolean verify(final char[] password, final String encryptedPassword) {
        byte passwordData[];
        try {
            passwordData = new String(password).getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 Unsupported");
        }
        MessageDigest messageDigest = null;
        int size = 0;
        String base64 = null;
        if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
            base64 = encryptedPassword.substring(5);
            try {
                messageDigest = MessageDigest.getInstance("SHA-1");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            base64 = encryptedPassword.substring(6);
            try {
                messageDigest = MessageDigest.getInstance("SHA-1");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
            base64 = encryptedPassword.substring(5);
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
            base64 = encryptedPassword.substring(6);
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else {
            return false;
        }
        final byte[] data = Base64.decode(base64.toCharArray());
        final byte[] orig = new byte[size];
        final short iterations = (short) ((data[0] & 0xff) << 8 | data[1] & 0xff);
        System.arraycopy(data, 2, orig, 0, size);
        byte[] salt = null;
        if (data.length > size + 2) {
            salt = new byte[data.length - size - 2];
            System.arraycopy(data, size + 2, salt, 0, data.length - size - 2);
        }
        final byte[] digest = new byte[size];
        for (int i = 0; i < iterations; i++) {
            messageDigest.reset();
            if (i > 0) {
                messageDigest.update(digest);
            }
            messageDigest.update(passwordData);
            if (salt != null && salt.length > 0) {
                messageDigest.update(salt);
            }
            System.arraycopy(messageDigest.digest(), 0, digest, 0, size);
        }
        return MessageDigest.isEqual(digest, orig);
    }
