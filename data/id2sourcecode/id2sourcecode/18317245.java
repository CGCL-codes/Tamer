    public UrlZip(String rootPath, String fName, ResourceBridge rb) throws Exception {
        super(rootPath, fName, rb);
        ZipInputStream zis = new ZipInputStream(super.getInputStream(fName));
        Vector v = new Vector();
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zis.read(buffer, 0, 1024)) > 0) baos.write(buffer, 0, bytesRead);
            v.add(new UrlZipEntry(entry, baos.toByteArray()));
            zis.closeEntry();
        }
        zis.close();
        entries = (UrlZipEntry[]) v.toArray(new UrlZipEntry[v.size()]);
    }
