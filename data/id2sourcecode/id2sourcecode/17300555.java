    public static List<String> searchFileFromClassPath(URL url, String classPath, String pattern) throws IOException {
        logger.debug("search file type: [" + pattern + "]");
        logger.debug("search file in classpath: [" + classPath + "]");
        logger.debug("the real filepath is : [" + url + "]");
        if (url == null) return new ArrayList<String>();
        String file = url.getFile();
        int i = file.indexOf("!");
        file = (i != -1) ? file.substring(0, i) : file;
        String protocol = url.getProtocol();
        if ("jar".equals(protocol)) {
            JarURLConnection jc = (JarURLConnection) url.openConnection();
            logger.debug("search jar file from :[" + url + "]");
            return searchFileFromZip(jc.getJarFile(), pattern, classPath, "");
        } else if ("wsjar".equals(protocol)) {
            if (file.startsWith("file:/")) file = file.substring(6);
            logger.debug("search wsjar file from :[" + file + "]");
            JarFile jarFile = new JarFile(new File(URLDecoder.decode(file, "UTF-8")));
            return searchFileFromZip(jarFile, pattern, classPath, "");
        } else if ("zip".equals(protocol)) {
            if (file.endsWith("war")) {
                logger.debug("search war file from :[" + file + "]");
                ZipFile zipFile = new ZipFile(new File(URLDecoder.decode(file, "UTF-8")));
                return searchFileFromZip(zipFile, pattern, classPath, "/WEB-INF/classes");
            } else {
                logger.debug("search zip file from :[" + file + "]");
                ZipFile zipFile = new ZipFile(new File(URLDecoder.decode(file, "UTF-8")));
                return searchFileFromZip(zipFile, pattern, classPath, "");
            }
        } else if ("file".equals(protocol)) {
            logger.debug("search filesystem folder from :[" + url + "]");
            return searchFileFromFolder(new File(URLDecoder.decode(url.getFile(), "UTF-8")), pattern, classPath);
        } else return new ArrayList<String>();
    }
