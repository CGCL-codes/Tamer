    public static String loadFileHash(String path) throws Exception {
        File f = new File(path);
        if (!(f.isFile() && f.exists())) {
            return null;
        }
        MessageDigest m = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(path);
        boolean done = false;
        byte buf[] = new byte[1024];
        while (!done) {
            int read = is.read(buf, 0, 1024);
            if (read == -1) {
                done = true;
            } else {
                m.update(buf, 0, read);
            }
        }
        is.close();
        byte dig[] = m.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dig.length; i++) {
            sb.append(toHexChar((dig[i] >>> 4) & 0x0F));
            sb.append(toHexChar(dig[i] & 0x0F));
        }
        return sb.toString();
    }
