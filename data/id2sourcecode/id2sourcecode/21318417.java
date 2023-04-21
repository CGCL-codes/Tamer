    @SuppressWarnings("unchecked")
    public static void unzipDir(File target, File archive) throws IOException {
        ZipFile zip = new ZipFile(archive);
        Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = (ZipEntry) entries.nextElement();
            File f = new File(target + File.separator + e.getName());
            if (e.isDirectory()) {
                if (!f.exists() && !f.mkdirs()) throw new IOException("Couldn't create directory " + f);
            } else {
                BufferedInputStream is = null;
                BufferedOutputStream os = null;
                try {
                    is = new BufferedInputStream(zip.getInputStream(e));
                    File destDir = f.getParentFile();
                    if (!destDir.exists() && !destDir.mkdirs()) throw new IOException("Couldn't create directory " + destDir);
                    os = new BufferedOutputStream(new FileOutputStream(f));
                    int b = -1;
                    while ((b = is.read()) != -1) os.write(b);
                } finally {
                    if (is != null) is.close();
                    if (os != null) os.close();
                }
            }
        }
    }
