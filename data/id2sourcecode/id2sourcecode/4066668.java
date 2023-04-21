    public static void unzip(String zipFilename, String outdir) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            byte[] buffer = new byte[1024];
            int len;
            InputStream zipin = zipFile.getInputStream(entry);
            BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(outdir + "\\" + entry.getName()));
            while ((len = zipin.read(buffer)) >= 0) fileout.write(buffer, 0, len);
            zipin.close();
            fileout.flush();
            fileout.close();
        }
    }
