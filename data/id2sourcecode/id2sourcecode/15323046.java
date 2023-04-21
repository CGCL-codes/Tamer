    public static String MD5Hash(String passwd) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte md5bytes[] = md5.digest(passwd.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream(md5bytes.length * 2);
        HexDump.dump(md5bytes, 0L, baos, 0);
        String rawdump = new String(baos.toByteArray());
        String hexchars = rawdump.substring(9, 57);
        hexchars = hexchars.trim();
        StringTokenizer tok = new StringTokenizer(hexchars, "  ");
        StringBuffer result = new StringBuffer();
        while (tok.hasMoreTokens()) {
            result.append(tok.nextToken());
        }
        return result.toString().toLowerCase();
    }
