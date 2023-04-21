    public void addJarContent(String file) throws Exception {
        sendMsg("Adding a jar file content ...");
        JarFile jar = new JarFile(file);
        Enumeration entries = jar.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zentry = (ZipEntry) entries.nextElement();
            try {
                InputStream zin = jar.getInputStream(zentry);
                outJar.putNextEntry(new ZipEntry(zentry.getName()));
                copyStream(zin, outJar);
                outJar.closeEntry();
                zin.close();
            } catch (ZipException zerr) {
            }
        }
    }
