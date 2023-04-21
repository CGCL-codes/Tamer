    public synchronized byte[] encryptAsByte(byte[] salt, String passwd) throws PasswordSecureException {
        digest.reset();
        if (salt == null || passwd == null) {
            Code.debug("Null salt or password entered");
            Log.event(Logs.encryptionFailure, new PasswordSecureException("Null salt or password"));
            throw new PasswordSecureException("Null salt or password passed");
        }
        byte[] cleartext = prependSalt(salt, passwd);
        byte[] digested = digest.digest(cleartext);
        byte[] encryptedPassword = prependSalt(salt, digested);
        return encryptedPassword;
    }
