    @RequestMapping(method = RequestMethod.GET, value = "/corpora/export.zip")
    public void findAllCorporaZiped(OutputStream outputStream) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream));
        CorporaEntryList corporaEntryList = spntEchoRepository.findAll();
        for (CorporaEntry entry : corporaEntryList) {
            GridFSDBFile audioForOutput = spntEchoRepository.findOutputById(entry.getObjectId().toString());
            if (audioForOutput != null) {
                zos.putNextEntry(new ZipEntry(entry.getFileName()));
                audioForOutput.writeTo(zos);
                zos.putNextEntry(new ZipEntry(entry.get_id() + ".xml"));
                jaxb2Marshaller.marshal(entry, new StreamResult(zos));
            }
        }
        zos.close();
    }
