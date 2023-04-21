    protected static String encodePassword(String algorithm, String clearTextPwd, byte[] saltBytes) throws PwdEncryptorException {
        try {
            if (algorithm.equals(TYPE_SSHA)) {
                byte[] clearTextPwdBytes = clearTextPwd.getBytes(Digester.ENCODING);
                byte[] pwdPlusSalt = new byte[clearTextPwdBytes.length + saltBytes.length];
                System.arraycopy(clearTextPwdBytes, 0, pwdPlusSalt, 0, clearTextPwdBytes.length);
                System.arraycopy(saltBytes, 0, pwdPlusSalt, clearTextPwdBytes.length, saltBytes.length);
                MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
                byte[] pwdPlusSaltHash = sha1Digest.digest(pwdPlusSalt);
                byte[] digestPlusSalt = new byte[pwdPlusSaltHash.length + saltBytes.length];
                System.arraycopy(pwdPlusSaltHash, 0, digestPlusSalt, 0, pwdPlusSaltHash.length);
                System.arraycopy(saltBytes, 0, digestPlusSalt, pwdPlusSaltHash.length, saltBytes.length);
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(digestPlusSalt);
            } else {
                return Digester.digest(algorithm, clearTextPwd);
            }
        } catch (NoSuchAlgorithmException nsae) {
            throw new PwdEncryptorException(nsae.getMessage());
        } catch (UnsupportedEncodingException uee) {
            throw new PwdEncryptorException(uee.getMessage());
        }
    }
