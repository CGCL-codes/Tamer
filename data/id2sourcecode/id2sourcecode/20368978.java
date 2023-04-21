    public void extract() throws IOException, java.util.zip.ZipException {
        this.print();
        java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
        java.util.Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            java.util.jar.JarEntry file = (java.util.jar.JarEntry) entries.nextElement();
            File f;
            if (file.isDirectory()) {
                @SuppressWarnings("unused") boolean success = new File(destDir + File.separator + file.getName()).mkdir();
                continue;
            } else {
                f = new File(destDir, file.getName());
            }
            FluxViz.getLogger().fine(f.toString());
            File dir = new File(f.getParent());
            if (!dir.exists()) {
                @SuppressWarnings("unused") boolean success = dir.mkdirs();
            }
            java.io.InputStream is = jar.getInputStream(file);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }
    }
