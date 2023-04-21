    public void zipEntries(OutputStream os) throws IOException {
        ZipOutputStream zipos = new ZipOutputStream(os);
        zipos.setMethod(ZipOutputStream.DEFLATED);
        for (int i = 0; i < oasisZipEntries.size(); i++) {
            OasisZipEntry oasisZipEntry = (OasisZipEntry) oasisZipEntries.get(i);
            ZipEntry zipEntry = new ZipEntry(oasisZipEntry.getName());
            zipos.putNextEntry(zipEntry);
            oasisZipEntry.writeData(zipos);
        }
        zipos.flush();
        zipos.finish();
    }
