    protected passwordDetails setPassword(int handler, int pw_type, char[] pw_chars, long timeout) throws CryptoManagerException {
        try {
            String persist_timeout_key = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_timeout";
            String persist_pw_key = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_value";
            String persist_pw_key_type = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_type";
            byte[] salt = getPasswordSalt();
            byte[] pw_bytes = new String(pw_chars).getBytes("UTF8");
            SHA1 sha1 = new SHA1();
            sha1.update(ByteBuffer.wrap(salt));
            sha1.update(ByteBuffer.wrap(pw_bytes));
            String encoded_pw = ByteFormatter.encodeString(sha1.digest());
            COConfigurationManager.setParameter(persist_timeout_key, timeout);
            COConfigurationManager.setParameter(persist_pw_key_type, pw_type);
            COConfigurationManager.setParameter(persist_pw_key, encoded_pw);
            passwordDetails result = new passwordDetails(encoded_pw.toCharArray(), pw_type);
            return (result);
        } catch (Throwable e) {
            throw (new CryptoManagerException("setPassword failed", e));
        }
    }
