    public void dumpAlbum(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        List<Media> list = getMediaList();
        for (Media m : list) {
            ZipEntry ze = new ZipEntry(m.file.getName());
            ze.setCrc(Utils.getCRC(m.file));
            ze.setMethod(ZipEntry.STORED);
            ze.setSize(m.file.length());
            zos.putNextEntry(ze);
            Utils.serveFile(m.file.getAbsolutePath(), zos);
            zos.closeEntry();
        }
        zos.flush();
        zos.finish();
        zos.close();
    }
