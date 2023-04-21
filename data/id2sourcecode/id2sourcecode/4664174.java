    public InputStream getInputStream(String entName) throws IOException {
        FileInputStream fis = new FileInputStream(epubFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals(entName)) {
                Utils.writeTo(zis, baos);
                break;
            }
        }
        zis.close();
        int nt = getSpineIndex(entName);
        if (nt >= 0) lastServedSpine = nt;
        return new ByteArrayInputStream(baos.toByteArray());
    }
