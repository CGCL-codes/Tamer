    protected void processZipEntry(final ZipInputStream input, final ZipOutputStream output, final ZipEntry entry) throws IOException {
        output.putNextEntry(cloneZipEntry(entry));
        if (!entry.isDirectory()) {
            final String name = entry.getName();
            if (isClass(name)) {
                processClass(name, input, output);
            } else if (isZip(name)) {
                processNestedZip(name, input, output);
            } else {
                processResource(name, input, output);
            }
        }
        output.closeEntry();
        input.closeEntry();
    }
