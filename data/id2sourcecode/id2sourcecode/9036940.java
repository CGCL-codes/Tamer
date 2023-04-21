    public static void uncompress(byte[] whatToUncompress, OutputStream os) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(whatToUncompress);
        ZipInputStream gzis = new ZipInputStream(bais);
        ZipEntry zipentry = gzis.getNextEntry();
        Integer.parseInt(zipentry.getName());
        byte[] buf = new byte[512];
        int bread;
        while ((bread = gzis.read(buf)) != -1) os.write(buf, 0, bread);
        gzis.closeEntry();
        gzis.close();
    }
