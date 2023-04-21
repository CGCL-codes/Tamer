    public static String getLocalHash(File f) {
        try {
            if (!f.exists()) return null;
            MessageDigest md;
            md = MessageDigest.getInstance("SHA1");
            byte[] b = new byte[1024];
            InputStream in = new FileInputStream(f);
            for (int n = 0; (n = in.read(b)) > -1; ) {
                md.update(b, 0, n);
            }
            byte[] digest = md.digest();
            String ret = "";
            for (int i = 0; i < digest.length; i++) {
                String tmp = Integer.toHexString(digest[i] & 0xFF);
                if (tmp.length() < 2) tmp = "0" + tmp;
                ret += tmp;
            }
            in.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
