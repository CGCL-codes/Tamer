    public static String getReleaseTag(Class clas) throws IOException {
        Properties props = new Properties();
        String clasFile = clas.getName().replaceAll("\\.", "/") + ".class";
        URL url = clas.getClassLoader().getResource(clasFile);
        if (url != null) {
            String surl = url.toString();
            url = new URL(new URL(surl.substring(0, surl.length() - clasFile.length())), "META-INF/build.txt");
            props.load(url.openStream());
            String tagName = props.getProperty("build.tag");
            if (tagName != null && tagName.trim().length() > 0) {
                return tagName;
            }
        }
        return null;
    }
