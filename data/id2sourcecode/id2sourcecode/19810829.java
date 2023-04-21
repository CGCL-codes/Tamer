    public static File extractTempZipFromJarFile(Class clazz, String zipFilename, String suffix) throws Exception {
        URL url = clazz.getResource(zipFilename);
        InputStream in = url.openConnection().getInputStream();
        File file = File.createTempFile("testZipFile", "." + suffix, new File(System.getProperty("java.io.tmpdir")));
        FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
        file.deleteOnExit();
        return file;
    }
