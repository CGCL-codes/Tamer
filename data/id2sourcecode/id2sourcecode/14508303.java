    @Override
    public OutputStream getEncoderStream(OutputStream target) throws IOException {
        ZipOutputStream output = new ZipOutputStream(target);
        output.putNextEntry(new ZipEntry(entryName));
        return output;
    }
