    private static String sha512Encrypt(final User user, final String password) {
        final StringBuilder passwordClone = new StringBuilder(password);
        passwordClone.append(user.getUsername());
        final byte[] defaultBytes = passwordClone.toString().getBytes();
        String retval = null;
        try {
            final MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(defaultBytes);
            final byte[] messageDigest = algorithm.digest();
            final StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(ENCRYPT_KEY & messageDigest[i]));
            }
            retval = hexString.toString();
        } catch (final NoSuchAlgorithmException nsae) {
            logger.debug(nsae.getMessage(), nsae);
        }
        return retval;
    }
