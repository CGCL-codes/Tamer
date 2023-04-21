    public void addEntry(InputStream jis, JarEntry entry) throws IOException, URISyntaxException {
        outputStreamHolder.putNextEntry(entry);
        if (!entry.isDirectory()) {
            readwriteStreams(jis, outputStreamHolder);
        }
        outputStreamHolder.closeEntry();
    }
