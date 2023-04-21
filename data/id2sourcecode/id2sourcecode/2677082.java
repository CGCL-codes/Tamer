    public static InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            logger.error("配置文件" + resourceName + "没有找到! ", e);
            return null;
        }
    }
