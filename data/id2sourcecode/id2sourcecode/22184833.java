    static Date getLastModified(final URL url) throws IOException {
        long result = 0;
        if ("jar".equalsIgnoreCase(url.getProtocol())) {
            String urlStr = url.toExternalForm();
            int p = urlStr.indexOf("!/");
            if (p != -1) {
                return getLastModified(new URL(urlStr.substring(4, p)));
            }
        }
        File sourceFile = IoUtil.url2file(url);
        if (sourceFile != null) {
            result = sourceFile.lastModified();
        } else {
            URLConnection cnn = url.openConnection();
            try {
                cnn.setUseCaches(false);
                cnn.setDoInput(false);
                result = cnn.getLastModified();
            } finally {
                try {
                    cnn.getInputStream().close();
                } catch (IOException ioe) {
                }
            }
        }
        if (result == 0) {
            throw new IOException("can't retrieve modification date for resource " + url);
        }
        Calendar cldr = Calendar.getInstance(Locale.ENGLISH);
        cldr.setTime(new Date(result));
        cldr.set(Calendar.MILLISECOND, 0);
        return cldr.getTime();
    }
