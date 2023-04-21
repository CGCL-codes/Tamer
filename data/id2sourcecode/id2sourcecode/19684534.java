    public static char[] cryptPassword(char pwd[]) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        byte pwdb[] = new byte[pwd.length];
        for (int b = 0; b < pwd.length; b++) pwdb[b] = (byte) pwd[b];
        char crypt[] = hexDump(md.digest(pwdb));
        smudge(pwdb);
        return crypt;
    }
