    public static byte[] uncompress(byte[] whatToUncompress) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(whatToUncompress);
        ZipInputStream gzis = new ZipInputStream(bais);
        ZipEntry zipentry = gzis.getNextEntry();
        int len = Integer.parseInt(zipentry.getName());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int bread;
        while ((bread = gzis.read(buf)) != -1) baos.write(buf, 0, bread);
        gzis.closeEntry();
        gzis.close();
        return baos.toByteArray();
    }
