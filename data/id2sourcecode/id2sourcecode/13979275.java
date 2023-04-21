    private static void writeJarEntry(JarEntry je, JarInputStream jarInputStream, JarOutputStream jos) throws IOException {
        jos.putNextEntry(je);
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = jarInputStream.read(buffer)) > 0) {
            jos.write(buffer, 0, read);
        }
        jos.closeEntry();
    }
