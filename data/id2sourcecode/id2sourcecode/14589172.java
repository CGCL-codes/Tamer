    private static void writeJarEntry(JarEntry je, JarFile jarFile, JarOutputStream jos) throws IOException {
        jos.putNextEntry(je);
        byte[] buffer = new byte[2048];
        int read = 0;
        InputStream is = jarFile.getInputStream(je);
        while ((read = is.read(buffer)) > 0) jos.write(buffer, 0, read);
        jos.closeEntry();
    }
