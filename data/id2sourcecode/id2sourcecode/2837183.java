    public void write(File directory, Dataset dataset) throws IOException {
        Metadata metadata = dataset.getMetadata();
        String name = metadata.getName() + "." + getExtension();
        OutputStream out = new FileOutputStream(new File(directory, name + (isCompressed() ? ".zip" : "")));
        if (isCompressed()) {
            out = new ZipOutputStream(out);
            ((ZipOutputStream) out).putNextEntry(new ZipEntry(name));
        }
        write(new OutputStreamWriter(out), dataset);
    }
