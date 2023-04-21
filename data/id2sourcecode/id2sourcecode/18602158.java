    public long getComputedDownloadSize() throws IOException {
        OutputStreamCounter osc = new OutputStreamCounter();
        ZipOutputStream zos = new ZipOutputStream(osc);
        List<Media> list = getMediaList();
        if (list == null || list.size() == 0) return 0;
        for (Media m : list) {
            ZipEntry ze = new ZipEntry(m.file.getName());
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(Utils.getDummyCRC(m.file.length()));
            ze.setSize(m.file.length());
            zos.putNextEntry(ze);
            Utils.serveDummyData(m.file.length(), zos);
            zos.closeEntry();
        }
        zos.flush();
        zos.finish();
        zos.close();
        return osc.getCount();
    }
