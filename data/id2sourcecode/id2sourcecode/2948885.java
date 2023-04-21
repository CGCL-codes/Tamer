    public JarToSPK(URL jarURL, long seed) throws IOException {
        super(seed);
        URL url = new URL("jar:" + jarURL + "!/");
        JarURLConnection juc = (JarURLConnection) url.openConnection();
        jar = juc.getJarFile();
    }
