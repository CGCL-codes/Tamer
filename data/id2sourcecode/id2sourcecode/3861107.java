    public String encryptPassword(String password) throws JetspeedSecurityException {
        if (securePasswords == false) {
            return password;
        }
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(passwordsAlgorithm);
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            ByteArrayOutputStream bas = new ByteArrayOutputStream(digest.length + digest.length / 3 + 1);
            OutputStream encodedStream = MimeUtility.encode(bas, "base64");
            encodedStream.write(digest);
            encodedStream.flush();
            encodedStream.close();
            return bas.toString();
        } catch (Exception e) {
            logger.error("Unable to encrypt password." + e.getMessage(), e);
            return null;
        }
    }
