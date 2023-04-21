    protected passwordDetails getPassword(int handler, int action, String reason, passwordTester tester, int pw_type) throws CryptoManagerException {
        final String persist_timeout_key = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_timeout";
        final String persist_pw_key = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_value";
        final String persist_pw_key_type = CryptoManager.CRYPTO_CONFIG_PREFIX + "pw." + handler + ".persist_type";
        long current_timeout = COConfigurationManager.getLongParameter(persist_timeout_key, 0);
        if (current_timeout < 0) {
            passwordDetails pw = (passwordDetails) session_passwords.get(persist_pw_key);
            if (pw != null && pw.getHandlerType() == pw_type) {
                return (pw);
            }
        }
        if (current_timeout > SystemTime.getCurrentTime()) {
            String current_pw = COConfigurationManager.getStringParameter(persist_pw_key, "");
            if (current_pw.length() > 0) {
                int type = (int) COConfigurationManager.getLongParameter(persist_pw_key_type, CryptoManagerPasswordHandler.HANDLER_TYPE_USER);
                if (type == pw_type) {
                    return (new passwordDetails(current_pw.toCharArray(), type));
                }
            }
        }
        Iterator it = password_handlers.iterator();
        while (it.hasNext()) {
            int retry_count = 0;
            char[] last_pw_chars = null;
            CryptoManagerPasswordHandler provider = (CryptoManagerPasswordHandler) it.next();
            if (pw_type != CryptoManagerPasswordHandler.HANDLER_TYPE_UNKNOWN && pw_type != provider.getHandlerType()) {
                continue;
            }
            while (retry_count < 64) {
                try {
                    CryptoManagerPasswordHandler.passwordDetails details = provider.getPassword(handler, action, retry_count > 0, reason);
                    if (details == null) {
                        break;
                    }
                    char[] pw_chars = details.getPassword();
                    if (last_pw_chars != null && Arrays.equals(last_pw_chars, pw_chars)) {
                        retry_count++;
                        continue;
                    }
                    last_pw_chars = pw_chars;
                    byte[] salt = getPasswordSalt();
                    byte[] pw_bytes = new String(pw_chars).getBytes("UTF8");
                    SHA1 sha1 = new SHA1();
                    sha1.update(ByteBuffer.wrap(salt));
                    sha1.update(ByteBuffer.wrap(pw_bytes));
                    String encoded_pw = ByteFormatter.encodeString(sha1.digest());
                    if (tester != null && !tester.testPassword(encoded_pw.toCharArray())) {
                        retry_count++;
                        continue;
                    }
                    int persist_secs = details.getPersistForSeconds();
                    long timeout;
                    if (persist_secs == 0) {
                        timeout = 0;
                    } else if (persist_secs == Integer.MAX_VALUE) {
                        timeout = Long.MAX_VALUE;
                    } else if (persist_secs < 0) {
                        timeout = -1;
                    } else {
                        timeout = SystemTime.getCurrentTime() + persist_secs * 1000L;
                    }
                    passwordDetails result = new passwordDetails(encoded_pw.toCharArray(), provider.getHandlerType());
                    synchronized (this) {
                        COConfigurationManager.setParameter(persist_timeout_key, timeout);
                        COConfigurationManager.setParameter(persist_pw_key_type, provider.getHandlerType());
                        session_passwords.remove(persist_pw_key);
                        COConfigurationManager.removeParameter(persist_pw_key);
                        if (timeout < 0) {
                            session_passwords.put(persist_pw_key, result);
                        } else if (timeout > 0) {
                            COConfigurationManager.setParameter(persist_pw_key, encoded_pw);
                            addPasswordTimer(persist_timeout_key, persist_pw_key, timeout);
                        }
                    }
                    provider.passwordOK(handler, details);
                    return (result);
                } catch (Throwable e) {
                    Debug.printStackTrace(e);
                    break;
                }
            }
        }
        throw (new CryptoManagerPasswordException(false, "No password handlers returned a password"));
    }
