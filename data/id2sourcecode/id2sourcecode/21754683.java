    private static void addFileEntry(JarOutputStream jos, InputStream is, String entryName) throws IOException {
        if (jos == null) throw new IllegalArgumentException("OutputStream is null.");
        if (is == null) throw new IllegalArgumentException("InputStream is null.");
        if (entryName == null || entryName.trim().length() == 0) throw new IllegalArgumentException("entryName is empty.");
        String availablePath = toAvailableJarPath(entryName);
        if (availablePath.length() == 0) return;
        jos.putNextEntry(new JarEntry(toAvailableJarPath(entryName)));
        byte buf[] = new byte[1024];
        int count;
        while ((count = is.read(buf, 0, buf.length)) != -1) jos.write(buf, 0, count);
        jos.closeEntry();
    }
