    protected static void loadTmpLibrary(String libsource, String prefix, String suffix) throws IOException {
        File libfile = File.createTempFile(prefix, suffix);
        InputStream libinp = ClassLoader.getSystemClassLoader().getSystemResourceAsStream(libsource);
        OutputStream libout = new FileOutputStream(libfile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = libinp.read(buf)) > 0) libout.write(buf, 0, len);
        libinp.close();
        libout.close();
        libfile.setExecutable(true);
        System.load(libfile.getAbsolutePath());
        libfile.deleteOnExit();
    }
