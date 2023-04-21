    public void writeTo(ZipOutputStream stream) throws IOException {
        ZipEntry versions = new ZipEntry(VHEADER);
        stream.putNextEntry(versions);
        XMLEncoder encoder = new XMLEncoder(stream);
        encoder.writeObject(this);
        encoder.close();
    }
