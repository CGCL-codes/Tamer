    private void parseJar(String jarPath, String filePath) {
        String filePathBuf = null;
        if (filePath.endsWith(".class")) filePathBuf = filePath; else filePathBuf = filePath + ".class";
        if (jarPath == null) {
            _log.error("Impossible de d�terminer le path de " + filePath);
            return;
        }
        try {
            filePathBuf = changeToJarSeparator(filePathBuf);
            URL url = new URL("jar:file:/" + jarPath + "!/" + filePathBuf);
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            JarFile jarfile = conn.getJarFile();
            JarEntry jarEntry = jarfile.getJarEntry(filePathBuf);
            parse(jarfile.getInputStream(jarEntry), (int) jarEntry.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
