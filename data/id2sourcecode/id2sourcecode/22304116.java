    private JarFile maybeGetJarFile(URL url) {
        String path;
        try {
            path = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new XBayaRuntimeException(e);
        }
        logger.finest("path: " + path);
        if (path.endsWith("/")) {
            return null;
        } else if ("file".equals(url.getProtocol())) {
            try {
                JarFile jarFile = new JarFile(path);
                return jarFile;
            } catch (IOException e) {
                throw new XBayaRuntimeException(e);
            }
        } else {
            try {
                if (this.tmpJarDirectory == null) {
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss-S");
                    String time = format.format(date);
                    String fileName = ".xbaya-jars-" + time;
                    String tmpdir = System.getProperty("java.io.tmpdir");
                    this.tmpJarDirectory = new File(tmpdir, fileName);
                    this.tmpJarDirectory.mkdir();
                }
                int i = path.lastIndexOf('/');
                File file = new File(this.tmpJarDirectory, path.substring(i + 1));
                logger.finest("file: " + file);
                InputStream stream = url.openStream();
                IOUtil.writeToFile(stream, file);
                JarFile jarFile = new JarFile(file);
                return jarFile;
            } catch (IOException e) {
                throw new XBayaRuntimeException(e);
            }
        }
    }
