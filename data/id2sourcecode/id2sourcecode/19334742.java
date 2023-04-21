    public static final byte[] getSHA1(File f) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("SHA-1");
            byte[] buf = new byte[65536];
            int num_read;
            InputStream in = new BufferedInputStream(new FileInputStream(f));
            while ((num_read = in.read(buf)) != -1) {
                m.update(buf, 0, num_read);
            }
            in.close();
            return m.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
