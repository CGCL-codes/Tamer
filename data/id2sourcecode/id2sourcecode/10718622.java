    public OutputStream getCompressionOutputStream(final File fileIn) throws IOException {
        setFileOutStream(new FileOutputStream(fileIn));
        ZipOutputStream zipOut = new ZipOutputStream(getFileOutStream());
        ZipEntry entry = new ZipEntry(fileIn.getName().replaceAll(getExtension() + "$", ""));
        zipOut.putNextEntry(entry);
        return zipOut;
    }
