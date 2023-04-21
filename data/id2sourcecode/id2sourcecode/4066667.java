    public static void unzip(String zipFilename, String[] filenames, String outdir) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration entries = zipFile.entries();
        L1: while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            for (int i = 0; i < filenames.length; i++) {
                if (entry.getName().equals(filenames[i])) {
                    byte[] buffer = new byte[1024];
                    int len;
                    InputStream zipin = zipFile.getInputStream(entry);
                    BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(outdir + "\\" + filenames[i]));
                    while ((len = zipin.read(buffer)) >= 0) fileout.write(buffer, 0, len);
                    zipin.close();
                    fileout.flush();
                    fileout.close();
                    continue L1;
                }
            }
        }
    }
