    public final OutputStream addOutputJarEntry(ZipEntry ze) throws IOException, IllegalStateException {
        if (outputJar == null) {
            throw new IllegalStateException("output jar is null");
        }
        putNextEntry(ze);
        return outputJar;
    }
