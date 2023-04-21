    public static final void unzip(File zip, File extractTo, String fileToUnzip) throws IOException {
        ZipFile archive = new ZipFile(zip);
        Enumeration e = archive.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (entry.getName().equals(fileToUnzip)) {
                File file = new File(extractTo, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    InputStream in = archive.getInputStream(entry);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] buffer = new byte[8192];
                    int read;
                    while (-1 != (read = in.read(buffer))) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }
