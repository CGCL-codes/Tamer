    public void test_getInputStream_DeleteJarFileUsingURLConnection() throws Exception {
        String jarFileName = "file.jar";
        String entry = "text.txt";
        File file = new File(jarFileName);
        FileOutputStream jarFile = new FileOutputStream(jarFileName);
        JarOutputStream out = new JarOutputStream(new BufferedOutputStream(jarFile));
        JarEntry jarEntry = new JarEntry(entry);
        out.putNextEntry(jarEntry);
        out.write(new byte[] { 'a', 'b', 'c' });
        out.close();
        URL url = new URL("jar:file:" + jarFileName + "!/" + entry);
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        InputStream is = conn.getInputStream();
        is.close();
        assertTrue(file.delete());
    }
