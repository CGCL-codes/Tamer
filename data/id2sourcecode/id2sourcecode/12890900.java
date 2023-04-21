    public void updatePassword(String userId, String password) {
        DirContext ctx = LDAPUtil.getDirContext(env);
        String encryptedPassword = Encryptor.digest(password);
        try {
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", "{SHA}" + encryptedPassword));
            ctx.modifyAttributes("uid=" + userId, mods);
        } catch (NamingException ne) {
        }
    }
