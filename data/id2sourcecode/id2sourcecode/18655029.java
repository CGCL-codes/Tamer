    @Override
    public OutputStream getOutputStream(final String path) throws IOException {
        return new FilterOutputStream(getZipOutputStream()) {

            {
                ((ZipOutputStream) this.out).putNextEntry(new ZipEntry(path));
            }

            @Override
            public void close() throws IOException {
                flush();
                ((ZipOutputStream) this.out).closeEntry();
            }
        };
    }
