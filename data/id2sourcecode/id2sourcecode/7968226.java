    public String encrypt(final char[] password, final String algorithm, final byte[] salt, final short iterations) {
        byte passwordData[];
        try {
            passwordData = new String(password).getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 Unsupported");
        }
        final StringBuffer buffer = new StringBuffer();
        MessageDigest messageDigest = null;
        int size = 0;
        if ("SHA".equalsIgnoreCase(algorithm) || "SSHA".equalsIgnoreCase(algorithm)) {
            if (salt != null && salt.length > 0) {
                buffer.append("{SSHA}");
            } else {
                buffer.append("{SHA}");
            }
            try {
                messageDigest = MessageDigest.getInstance("SHA-1");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else if ("MD5".equalsIgnoreCase(algorithm) || "SMD5".equalsIgnoreCase(algorithm)) {
            if (salt != null && salt.length > 0) {
                buffer.append("{SMD5}");
            } else {
                buffer.append("{MD5}");
            }
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                size = messageDigest.getDigestLength();
            } catch (final NoSuchAlgorithmException e) {
                throw new IllegalStateException("Invalid algorithm");
            }
        } else {
            throw new UnsupportedOperationException("Not implemented");
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
        int outSize = size + 2;
        if (salt != null && salt.length > 0) {
            outSize += salt.length;
        }
        final byte[] out = new byte[outSize];
        out[0] = (byte) (iterations >>> 8 & 0xff);
        out[1] = (byte) (iterations & 0xff);
        System.arraycopy(digest, 0, out, 2, size);
        if (salt != null && salt.length > 0) {
            System.arraycopy(salt, 0, out, size + 2, salt.length);
        }
        buffer.append(new String(Base64.encode(out)));
        return buffer.toString();
    }
