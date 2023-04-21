    public void extractZip(byte[] file) throws Exception {
        byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new ByteArrayInputStream(file));
        new File(extractpath).mkdirs();
        zipentry = zipinputstream.getNextEntry();
        while (zipentry != null) {
            String entryName = zipentry.getName();
            int n;
            FileOutputStream fileoutputstream;
            File newFile = new File(entryName);
            String directory = newFile.getParent();
            if (directory != null) {
                new File(extractpath + File.separator + directory).mkdirs();
            }
            fileoutputstream = new FileOutputStream(extractpath + File.separator + entryName);
            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
            fileoutputstream.close();
            zipinputstream.closeEntry();
            zipentry = zipinputstream.getNextEntry();
        }
        zipinputstream.close();
    }
