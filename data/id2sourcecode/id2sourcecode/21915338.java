    public String computeHash(final String plainTextPassword, final byte[] salt) {
        String hashedPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(salt);
            hashedPassword = new String(digest.digest(plainTextPassword.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordProtectionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PasswordProtectionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hashedPassword;
    }
