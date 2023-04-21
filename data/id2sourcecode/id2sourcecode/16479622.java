    private static void addFile(JarOutputStream jout, String path, File file) throws IOException {
        jout.putNextEntry(new JarEntry(path));
        FileInputStream in = new FileInputStream(file);
        int read = in.read();
        while (read != -1) {
            jout.write(read);
            read = in.read();
        }
        jout.flush();
        jout.closeEntry();
    }
