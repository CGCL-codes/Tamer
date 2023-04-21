    public static void unzip(String src, String dest) throws IOException {
        if (!(new File(dest)).exists()) {
            (new File(dest)).mkdirs();
        }
        ZipFile zipFile = new ZipFile(src);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        byte[] buf = new byte[1024];
        int len;
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                File newDir = new File(dest, entry.getName().replace('/', File.separatorChar));
                newDir.mkdirs();
            }
        }
        entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            } else {
                InputStream in = zipFile.getInputStream(entry);
                File outFile = new File(dest, entry.getName().replace('/', File.separatorChar));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
                while ((len = in.read(buf)) >= 0) out.write(buf, 0, len);
                in.close();
                out.flush();
                out.close();
            }
        }
        zipFile.close();
    }
