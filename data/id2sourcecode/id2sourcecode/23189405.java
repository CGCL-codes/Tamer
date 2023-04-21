    protected void jarDirectory(JarOutputStream jarOut, String pathInJar, String actualPath) throws IOException {
        FileInputStream fin;
        byte[] buffer = new byte[4096];
        String[] files = new File(actualPath).list();
        if (pathInJar.length() > 0) {
            pathInJar += "/";
            jarOut.putNextEntry(new JarEntry(pathInJar));
            jarOut.closeEntry();
        }
        for (int i = 0; i < files.length; i++) {
            if (new File(actualPath + "/" + files[i]).isDirectory()) {
                jarDirectory(jarOut, pathInJar + files[i], actualPath + "/" + files[i]);
            } else {
                fin = new FileInputStream(actualPath + "/" + files[i]);
                jarOut.putNextEntry(new JarEntry(pathInJar + files[i]));
                int length;
                while ((length = fin.read(buffer)) > 0) jarOut.write(buffer, 0, length);
                jarOut.closeEntry();
                fin.close();
            }
        }
    }
