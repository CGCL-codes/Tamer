    private void copyInstallerClassFiles(JarOutputStream jarOut) throws IOException {
        JarInputStream jarIn = new JarInputStream(getClass().getResourceAsStream(INSTALLER_CLASS_FILES_JAR));
        JarEntry jarEntry;
        while ((jarEntry = jarIn.getNextJarEntry()) != null) {
            jarOut.putNextEntry(jarEntry);
            int read;
            byte[] block = new byte[2048];
            while ((read = jarIn.read(block)) != -1) {
                jarOut.write(block, 0, read);
            }
            jarOut.closeEntry();
        }
    }
