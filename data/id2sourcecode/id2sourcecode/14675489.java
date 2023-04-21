    private static void addFile(ZipOutputStream zipOut, File toZip) throws IOException {
        FileInputStream toZipOut = new FileInputStream(toZip);
        ByteArrayOutputStream memStream = new ByteArrayOutputStream();
        copy(toZipOut, memStream);
        toZipOut.close();
        long length = memStream.size();
        ZipEntry entry = new ZipEntry(toZip.getName());
        entry.setSize(length);
        zipOut.putNextEntry(entry);
        ByteArrayInputStream memInputStream = new ByteArrayInputStream(memStream.toByteArray());
        copy(memInputStream, zipOut);
        zipOut.closeEntry();
    }
