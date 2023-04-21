    public static String crypt(String salt, char password[]) {
        if (salt.length() > 8) salt = salt.substring(0, 8);
        byte slt[] = salt.getBytes();
        byte pwd[] = new byte[password.length];
        for (int i = 0; i < pwd.length; i++) {
            pwd[i] = (byte) password[i];
            password[i] = '\0';
        }
        MessageDigest md1 = getMD5();
        md1.update(pwd);
        md1.update(MAGIC.getBytes());
        md1.update(slt);
        MessageDigest md2 = getMD5();
        md2.update(pwd);
        md2.update(slt);
        md2.update(pwd);
        byte digest[] = md2.digest();
        for (int i = pwd.length; i > 0; i -= 16) {
            md1.update(digest, 0, i <= 16 ? i : 16);
        }
        for (int i = pwd.length; i > 0; i >>>= 1) {
            if ((i & 1) != 0) md1.update((byte) 0); else md1.update(pwd[0]);
        }
        digest = md1.digest();
        for (int i = 0; i < 1000; i++) {
            md1.reset();
            if ((i & 1) != 0) md1.update(pwd); else md1.update(digest);
            if (i % 3 != 0) md1.update(slt);
            if (i % 7 != 0) md1.update(pwd);
            if ((i & 1) != 0) md1.update(digest); else md1.update(pwd);
            digest = md1.digest();
        }
        for (int i = 0; i < pwd.length; i++) {
            pwd[i] = 0;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(MAGIC).append(salt).append('$');
        long l = ui(digest[0]) << 16 | ui(digest[6]) << 8 | ui(digest[12]);
        sb.append(a64(l, 4));
        l = ui(digest[1]) << 16 | ui(digest[7]) << 8 | ui(digest[13]);
        sb.append(a64(l, 4));
        l = ui(digest[2]) << 16 | ui(digest[8]) << 8 | ui(digest[14]);
        sb.append(a64(l, 4));
        l = ui(digest[3]) << 16 | ui(digest[9]) << 8 | ui(digest[15]);
        sb.append(a64(l, 4));
        l = ui(digest[4]) << 16 | ui(digest[10]) << 8 | ui(digest[5]);
        sb.append(a64(l, 4));
        l = ui(digest[11]);
        sb.append(a64(l, 2));
        return sb.toString();
    }
