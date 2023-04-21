    public long getComputedDownloadSize() throws IOException {
        OutputStreamCounter osc = new OutputStreamCounter();
        ZipOutputStream zos = new ZipOutputStream(osc);
        List<MediaFacade> list = getMediaList();
        if (list == null || list.size() == 0) return 0;
        for (MediaFacade m : list) {
            ZipEntry ze = new ZipEntry(m.getName());
            ze.setMethod(ZipEntry.STORED);
            ze.setCrc(Utils.getDummyCRC(m.getSize()));
            ze.setSize(m.getSize());
            zos.putNextEntry(ze);
            Utils.serveDummyData(m.getSize(), zos);
            zos.closeEntry();
        }
        zos.flush();
        zos.finish();
        zos.close();
        return osc.getCount();
    }
