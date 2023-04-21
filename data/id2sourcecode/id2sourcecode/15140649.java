    private JarFile getJarFile(URL bundleUrl) {
        Activator.traceMessage("Searching " + bundleUrl + " for tests.");
        JarFile jarFile = null;
        try {
            URL url = FileLocator.resolve(bundleUrl);
            jarFile = new JarFile(url.getPath());
        } catch (IOException e) {
            try {
                Activator.traceMessage("Could not open " + bundleUrl + " directly. It seems to be in a jar.");
                File file = File.createTempFile("bundleJar", ".jar");
                file.deleteOnExit();
                InputStream inputStream = FileLocator.openStream(getBundle(), new Path(bundleUrl.getPath()), false);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while (inputStream.available() > 0) {
                    int read = inputStream.read(buffer);
                    outputStream.write(buffer, 0, read);
                }
                outputStream.close();
                inputStream.close();
                jarFile = new JarFile(file);
            } catch (IOException e1) {
                Activator.traceMessage("Could not get content of  " + bundleUrl + ". " + e1.getMessage());
            }
        }
        return jarFile;
    }
