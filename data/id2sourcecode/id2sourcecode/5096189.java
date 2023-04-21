    private void addJarsToLibs(JarInputStream jarInputStream, File file) throws IOException {
        if (file.isDirectory()) {
            if (!file.exists()) {
                file.mkdirs();
            }
            for (File someFile : file.listFiles()) {
                addJarsToLibs(jarInputStream, someFile);
            }
            file.deleteOnExit();
        } else {
            file.deleteOnExit();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.getParentFile().deleteOnExit();
            }
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            ExtractorUtils.copyStream(jarInputStream, os);
            os.close();
            if (file.getName().endsWith(".jar")) {
                libraries.add(file);
            }
        }
    }
