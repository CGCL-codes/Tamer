    public boolean isEqual(@Nullable Password password, @Nullable String plainTextPassword) {
        if (password == null || plainTextPassword == null) {
            return false;
        }
        lock.lock();
        try {
            digest.update(password.getSalt());
            digest.update(plainTextPassword.getBytes("UTF-8"));
            return equals(password.getHash(), digest.digest());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
