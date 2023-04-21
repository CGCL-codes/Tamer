    private String getID(File file) {
        FileInputStream is = null;
        try {
            md = md == null ? MessageDigest.getInstance("MD5") : md;
            md.reset();
            is = new FileInputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                md.update(buf, 0, len);
            }
        } catch (Throwable t) {
            usage(t.getMessage());
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return new String(md.digest());
    }
