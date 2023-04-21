    public static String getFileHashFromXML(File xml, File file) throws Exception {
        FileReader in = null;
        try {
            in = new FileReader(xml);
            char[] c = new char[1024 * 10];
            int read = 0;
            StringBuffer buf = new StringBuffer();
            while (read < xml.length()) {
                int r = in.read(c);
                if (r <= 0) continue;
                read += r;
                buf.append(new String(c, 0, r));
                int p = buf.lastIndexOf("</sharedfile>");
                String later = buf.substring(p + "</sharedfile>".length());
                buf.delete(p, buf.length());
                p = buf.indexOf(file.getAbsolutePath());
                if (p >= 0) {
                    p = buf.indexOf("<filehash>", p);
                    p = buf.indexOf(">", p) + 1;
                    int e = buf.indexOf("<", p);
                    String hex = buf.substring(p, e);
                    return hex;
                }
                buf.delete(0, buf.length());
                buf.append(later);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) in.close();
        }
        MessageDigest md5 = MessageDigest.getInstance("SHA-512");
        FileInputStream fin = new FileInputStream(file);
        int read = 0;
        while (read < file.length()) {
            byte[] da = new byte[1024];
            int r = fin.read(da);
            read += r;
            md5.update(da, 0, r);
        }
        fin.close();
        byte[] digest = md5.digest();
        String hex = "";
        for (int i = 0; i < digest.length; i++) {
            int bb = digest[i] & 0xff;
            if (Integer.toHexString(bb).length() == 1) hex = hex + "0";
            hex = hex + Integer.toHexString(bb);
        }
        return hex.toUpperCase();
    }
