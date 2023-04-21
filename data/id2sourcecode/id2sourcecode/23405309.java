    private static File createJarFile(Manifest mf, Class<?>... classes) throws IOException {
        byte b[] = new byte[512];
        File jarFile = File.createTempFile("tmp_", ".jar");
        Reporter.log("Jar file: " + jarFile);
        jarFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(jarFile);
        JarOutputStream jout = new JarOutputStream(fos);
        JarEntry e = new JarEntry("META-INF/MANIFEST.MF");
        jout.putNextEntry(e);
        mf.write(jout);
        jout.closeEntry();
        for (Class<?> cls : classes) {
            e = new JarEntry(cls.getName().replace('.', '/') + ".class");
            jout.putNextEntry(e);
            int len = 0;
            URL url = cls.getResource("/" + e.getName());
            InputStream in = url.openStream();
            while ((len = in.read(b)) != -1) jout.write(b, 0, len);
            jout.closeEntry();
        }
        jout.close();
        return jarFile;
    }
