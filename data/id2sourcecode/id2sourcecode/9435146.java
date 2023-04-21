    public InputStream getInputStreamFromResource(String resourceName) throws SourceException {
        InputStream resultStream = null;
        try {
            if (OntoramaConfig.VERBOSE) {
                System.out.println("resourceName = " + resourceName);
            }
            URL url = OntoramaConfig.getClassLoader().getResource(resourceName);
            if (OntoramaConfig.VERBOSE) {
                System.out.println("url = " + url);
            }
            if (url.getProtocol().equalsIgnoreCase("jar")) {
                String pathString = url.getFile();
                int index = pathString.indexOf("!");
                String filePath = pathString.substring(0, index);
                if (filePath.startsWith("file")) {
                    int index1 = pathString.indexOf(":") + 1;
                    filePath = filePath.substring(index1, filePath.length());
                }
                File file = new File(filePath);
                ZipFile zipFile = new ZipFile(file);
                ZipEntry zipEntry = zipFile.getEntry(resourceName);
                resultStream = (InputStream) zipFile.getInputStream(zipEntry);
            } else if (url.getProtocol().equalsIgnoreCase("file")) {
                resultStream = url.openStream();
            } else {
                System.err.println("Dont' know about this protocol: " + url.getProtocol());
                System.exit(-1);
            }
        } catch (IOException ioExc) {
            throw new SourceException("Couldn't read input data source for " + resourceName + ", error: " + ioExc.getMessage());
        } catch (NullPointerException npe) {
            throw new SourceException("Coudn't load resource for " + resourceName + ", please check if resource exists at this location");
        }
        return resultStream;
    }
