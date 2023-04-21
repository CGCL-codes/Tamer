    public static URL validateURL(String fileName) {
        DozerClassLoader classLoader = BeanContainer.getInstance().getClassLoader();
        if (fileName == null) {
            MappingUtils.throwMappingException("File name is null");
        }
        URL url = classLoader.loadResource(fileName);
        if (url == null) {
            MappingUtils.throwMappingException("Unable to locate dozer mapping file [" + fileName + "] in the classpath!");
        }
        InputStream stream = null;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            MappingUtils.throwMappingException("Unable to open URL input stream for dozer mapping file [" + url + "]");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    MappingUtils.throwMappingException("Unable to close input stream for dozer mapping file [" + url + "]");
                }
            }
        }
        return url;
    }
