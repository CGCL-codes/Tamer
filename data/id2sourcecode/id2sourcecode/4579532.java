    public OutputStream getOutputStream() throws IOException {
        if (out == null) {
            out = new ZipOutputStream(super.getOutputStream());
            ((ZipOutputStream) out).putNextEntry(new ZipEntry("dummy"));
        }
        return out;
    }
