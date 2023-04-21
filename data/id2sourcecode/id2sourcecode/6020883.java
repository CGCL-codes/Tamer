    @NotNull
    public Password encrypt(@NotNull String password) {
        lock.lock();
        try {
            byte[] salt = new byte[32];
            random.nextBytes(salt);
            digest.update(salt);
            digest.update(password.getBytes("UTF-8"));
            return new Password(salt, digest.digest());
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
