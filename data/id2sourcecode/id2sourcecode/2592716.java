    public static void extractZip(File jarFile, File destDir) throws IOException {
        java.util.zip.ZipFile jar = new java.util.zip.ZipFile(jarFile);
        java.util.Enumeration en = jar.entries();
        while (en.hasMoreElements()) {
            java.util.zip.ZipEntry file = (java.util.zip.ZipEntry) en.nextElement();
            java.io.File f = new java.io.File(destDir + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            }
            f.getParentFile().mkdirs();
            java.io.InputStream is = jar.getInputStream(file);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }
    }
