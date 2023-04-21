    public ZipEntry beginEntry(final String name) throws IOException {
        ZipEntry result = new ZipEntry(name);
        zipOutput.putNextEntry(result);
        return result;
    }
