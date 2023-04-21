    public boolean writeAboutHtmlFile(File resource, JarOutputStream out) throws Exception {
        try {
            ZipEntry newEntry;
            InputStream inputStream;
            if (eclipseInstallPlugin.isJar()) {
                JarFile pluginJar = new JarFile(this.eclipseInstallPlugin.getPluginLocation());
                newEntry = pluginJar.getJarEntry(ABOUT_FILE);
                out.putNextEntry(newEntry);
                inputStream = pluginJar.getInputStream(newEntry);
            } else {
                newEntry = new ZipEntry(resource.getName());
                out.putNextEntry(newEntry);
                inputStream = new FileInputStream(resource);
            }
            writeToOutputStream(out, inputStream);
            out.closeEntry();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
