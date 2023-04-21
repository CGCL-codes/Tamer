    public void dumpAlbum(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        List<MediaFacade> list = getMediaList();
        for (MediaFacade m : list) {
            ZipEntry ze = new ZipEntry(m.getName());
            ze.setCrc(Utils.getCRC(m.getFile()));
            ze.setMethod(ZipEntry.STORED);
            ze.setSize(m.getSize());
            zos.putNextEntry(ze);
            Utils.serveFile(m.getAbsolutePath(), zos);
            zos.closeEntry();
        }
        zos.flush();
        zos.finish();
        zos.close();
    }
