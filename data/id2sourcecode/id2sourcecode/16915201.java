    public static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
        ZipFile zip = new ZipFile(zipfile);
        ZipOutputStream zos = new ZipOutputStream(out);
        @SuppressWarnings("unchecked") Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
        while (en.hasMoreElements()) {
            ZipEntry ze = en.nextElement();
            if (!ze.getName().equals(entry)) {
                zos.putNextEntry(new ZipEntry(ze.getName()));
                InputStream is = zip.getInputStream(ze);
                copyStream(is, zos);
                is.close();
            }
        }
        zos.putNextEntry(new ZipEntry(entry));
        InputStream is = new FileInputStream(tmpfile);
        copyStream(is, zos);
        is.close();
        zos.close();
    }
