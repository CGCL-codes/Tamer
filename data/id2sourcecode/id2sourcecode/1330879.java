    protected static void checkURL(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        JarInputStream jar = new JarInputStream(in);
        ZipEntry entry;
        while ((entry = jar.getNextEntry()) != null) {
            if (entry.isDirectory()) {
            } else {
                jarClasses.put(entry.getName().replace('/', '.'), url);
            }
        }
        in.close();
    }
