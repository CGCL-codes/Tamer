    private void generateMD5() throws PZException {
        buffer.clear();
        put(LINK_GET_MD5_SALT, buffer);
        writeAndRead();
        if (getCommand(buffer) != MASTER_MD5_SALT) throw new PZException("Unable to fetch MD5 salt");
        byte[] salt = getBytes(buffer);
        digest.reset();
        digest.update(salt);
        digest.update(password);
        this.md5 = digest.digest();
    }
