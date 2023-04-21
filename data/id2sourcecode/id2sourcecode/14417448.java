    public static void extractAll(InputStream zipStream, File destDir) throws IOException {
        byte[] buf = new byte[4096];
        ZipInputStream zipinputstream = new ZipInputStream(zipStream);
        ZipEntry zipentry;
        while ((zipentry = zipinputstream.getNextEntry()) != null) {
            String entryName = zipentry.getName();
            File newFile = new File(destDir, entryName);
            if (zipentry.isDirectory()) {
                newFile.mkdirs();
            } else {
                FileOutputStream fileoutputstream;
                fileoutputstream = new FileOutputStream(newFile);
                int n;
                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
                fileoutputstream.close();
                zipinputstream.closeEntry();
            }
        }
        zipinputstream.close();
    }
