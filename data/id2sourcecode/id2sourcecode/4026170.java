    protected void copyToJar(JarEntry entry, InputStream in, JarOutputStream jarOut) throws IOException {
        jarOut.putNextEntry(entry);
        int read;
        byte[] block = new byte[4096];
        while ((read = in.read(block)) != -1) {
            jarOut.write(block, 0, read);
        }
        jarOut.closeEntry();
    }
