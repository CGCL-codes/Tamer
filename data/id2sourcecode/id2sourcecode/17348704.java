    public static Properties load(Collection<URL> urls) throws IOException {
        Properties result = new Properties();
        for (URL url : urls) {
            InputStream is = url.openStream();
            try {
                if (url.getFile().endsWith(".xml")) {
                    result.loadFromXML(is);
                } else {
                    result.load(is);
                }
            } finally {
                is.close();
            }
        }
        return result;
    }
