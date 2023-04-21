    protected void write(String name, Object object) throws IOException {
        if (name == null) {
            throw new IllegalArgumentException("name == null");
        }
        if (object == null) {
            throw new IllegalArgumentException("object == null");
        }
        ZipEntry entry = new ZipEntry(name);
        ((ZipOutputStream) out).putNextEntry(entry);
        XMLEncoder encoder = new XMLEncoder(new XMLEncoderOutputStreamWrapper(out));
        encoder.writeObject(object);
        encoder.close();
        ((ZipOutputStream) out).closeEntry();
    }
