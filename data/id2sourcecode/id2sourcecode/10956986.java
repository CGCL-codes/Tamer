    private static final void addFileToZip(ZipOutputStream out, File file, String rootDirectory, byte[] buffer) throws IOException {
        LOG.debug("Adding file to zip: " + file.getPath());
        ZipEntry entry = new ZipEntry(file.getPath().substring(rootDirectory.length()));
        out.putNextEntry(entry);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
        ResourceUtils.stream2stream(in, out, buffer);
        in.close();
    }
