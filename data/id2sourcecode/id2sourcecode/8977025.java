    private String unJar(String jarPath, String jarEntry) {
        String path;
        if (jarPath.lastIndexOf("lib/") >= 0) path = jarPath.substring(0, jarPath.lastIndexOf("lib/")); else path = jarPath.substring(0, jarPath.lastIndexOf("/"));
        String relPath = jarEntry.substring(0, jarEntry.lastIndexOf("/"));
        try {
            new File(path + "/" + relPath).mkdirs();
            JarFile jar = new JarFile(jarPath);
            ZipEntry ze = jar.getEntry(jarEntry);
            InputStream finput = jar.getInputStream(ze);
            File bin = new File(path + "/" + jarEntry);
            FileOutputStream foutput = new FileOutputStream(bin);
            byte[] mBuffer = new byte[1024];
            int n;
            while ((n = finput.read(mBuffer)) > 0) foutput.write(mBuffer, 0, n);
            foutput.close();
            finput.close();
        } catch (Exception e) {
            log.error("Error unjaring " + jarEntry + " from " + jarEntry);
        }
        return path;
    }
