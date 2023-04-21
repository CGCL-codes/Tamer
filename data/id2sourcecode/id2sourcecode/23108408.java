    private static char[] cripta(char pwd[]) throws Exception {
        if (null == md) {
            md = MessageDigest.getInstance(ALGORITHM_SHA);
        }
        md.reset();
        byte pwdb[] = new byte[pwd.length];
        for (int b = 0; b < pwd.length; b++) {
            pwdb[b] = (byte) pwd[b];
        }
        char crypt[] = hexDump(md.digest(pwdb));
        smudge(pwdb);
        return crypt;
    }
