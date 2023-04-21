    protected boolean performAuth(String login, byte[] password) {
        if (!users.containsKey(login)) return false;
        byte[] hash = (byte[]) users.get(login);
        return ByteArrayTool.compare(hash, md.digest(password));
    }
