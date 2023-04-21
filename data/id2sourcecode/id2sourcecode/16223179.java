    File extractResource(String name) throws IOException {
        debug("extractResource " + name);
        URL url = bc.getBundle().getResource(name);
        String fname = name;
        File baseDir = getBaseDir();
        File f = new File(baseDir, fname);
        File dir = f.getParentFile();
        dir.mkdirs();
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        debug("extract " + name + " to " + f.getAbsolutePath());
        try {
            bin = new BufferedInputStream(url.openStream());
            bout = new BufferedOutputStream(new FileOutputStream(f));
            byte[] buf = new byte[1024 * 10];
            int n;
            while (-1 != (n = bin.read(buf))) {
                bout.write(buf, 0, n);
            }
            bout.flush();
            return f;
        } finally {
            try {
                bin.close();
            } catch (Exception ignored) {
            }
            try {
                bout.close();
            } catch (Exception ignored) {
            }
        }
    }
