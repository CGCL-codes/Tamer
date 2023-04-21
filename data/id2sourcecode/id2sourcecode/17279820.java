    public JarResourceLoader(URL url) throws IOException {
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        File file = File.createTempFile("rachel-", null);
        FileUtils.saveStreamToFile(in, file);
        JarFile jarFile = new JarFile(file, false);
        init(file, jarFile);
    }
