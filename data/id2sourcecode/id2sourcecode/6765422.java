    private void writeResourceToOutputDirectory(ResourceReference<JDomTestDocument.JDomTestElement> resourceRef, Class testClass, String outputPath) {
        String path = resourceRef.getResourcePath();
        URL url = new ResourceFinder(testClass).getResourceAsURL(path);
        if (url == null) {
            throw new XcordionBug("Cannot find resource: " + path);
        }
        path = path.replace('/', File.separatorChar);
        while (path.startsWith(Character.toString(File.separatorChar))) {
            path = path.substring(1);
        }
        File outputFile = new File(outputDirectory, path);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        resourceRef.setResourceReferenceUri(FileUtils.relativePath(outputPath, path, false, File.separatorChar));
        if (outputFile.exists() && System.currentTimeMillis() - outputFile.lastModified() < 5000) {
            return;
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = url.openStream();
            os = new FileOutputStream(outputFile);
            byte[] buffer = new byte[is.available()];
            int i;
            while ((i = is.read(buffer)) > 0) {
                os.write(buffer, 0, i);
            }
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
