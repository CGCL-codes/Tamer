    private static void copyStreamToJarStream(JarOutputStream jout, InputStream src) throws IOException {
        byte[] buf = new byte[512];
        do {
            int bread = src.read(buf);
            if (bread <= 0) break;
            jout.write(buf, 0, bread);
        } while (true);
        jout.closeEntry();
    }
