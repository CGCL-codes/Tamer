    private static void copy(URL url, File f) {
        try {
            InputStream in = url.openStream();
            FileOutputStream out = new FileOutputStream(f);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            Log.log(Log.ERROR, SuperAbbrevsIO.class, e);
        }
    }
