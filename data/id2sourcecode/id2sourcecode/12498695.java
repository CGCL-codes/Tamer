    public void addSecondaryOutputs(ZipOutputStream outputStream, List<SecondaryOutput> secondaryDownloadOutputs) throws Exception {
        for (SecondaryOutput sdo : secondaryDownloadOutputs) {
            outputStream.putNextEntry(new ZipEntry(sdo.getFileName()));
            sdo.process(outputStream);
            outputStream.closeEntry();
        }
    }
