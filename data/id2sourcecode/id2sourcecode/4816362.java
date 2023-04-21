    public OutputStream createOutputStream(OutputStream out) throws IOException {
        ZipOutputStream result = new ZipOutputStream(out);
        result.putNextEntry(new ZipEntry(entryName));
        return result;
    }
