    public static void unjar(File jarFile, String resource, File targetDir) throws FileNotFoundException, IOException {
        if (targetDir.exists()) {
            targetDir.delete();
        }
        targetDir.mkdirs();
        String targetPath = targetDir.getAbsolutePath() + File.separatorChar;
        byte[] buffer = new byte[1024 * 1024];
        JarFile input = new JarFile(jarFile, false, ZipFile.OPEN_READ);
        Enumeration<JarEntry> enumeration = input.entries();
        for (; enumeration.hasMoreElements(); ) {
            JarEntry entry = enumeration.nextElement();
            if (!entry.isDirectory()) {
                if (entry.getName().equals(resource)) {
                    String path = targetPath + entry.getName();
                    File file = new File(path);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    InputStream in = input.getInputStream(entry);
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    out.close();
                }
            }
        }
    }
