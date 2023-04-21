    public OutputStream getOutputStream() throws IOException {
        if (null == compressingOutputStream) {
            OutputStream originalOutputStream = super.getOutputStream();
            compressingOutputStream = new ZipOutputStream(originalOutputStream);
            ((ZipOutputStream) compressingOutputStream).putNextEntry(new ZipEntry("dummy"));
        }
        return compressingOutputStream;
    }
