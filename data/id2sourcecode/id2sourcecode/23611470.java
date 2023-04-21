    public static void readToFile(String url, String file) {
        OutputStream fos = null;
        URLConnection conn = null;
        try {
            File f = new File(file);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            fos = new BufferedOutputStream(new FileOutputStream(f), 32);
            conn = new URL(url).openConnection();
            InputStream ins = conn.getInputStream();
            int num = 0;
            byte[] buf = new byte[32 * 1024];
            while ((num = ins.read(buf)) != -1) {
                fos.write(buf, 0, num);
            }
        } catch (Exception e) {
            File f = new File(file);
            if (f.exists()) {
                f.delete();
            }
            throw new RuntimeException(e);
        } finally {
            doClose(fos);
        }
    }
