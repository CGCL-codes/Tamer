    public static String getReleaseTag() throws IOException {
        Enumeration<URL> urls = AboutUtil.class.getClassLoader().getResources("META-INF/build.txt");
        Properties props = new Properties();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            props.load(url.openStream());
            String tagName = props.getProperty("build.tag");
            if (tagName != null && tagName.trim().length() > 0) {
                return tagName;
            }
        }
        return null;
    }
