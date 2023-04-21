    public static List<String> getBuildInfos() throws IOException {
        ClassLoader loader = AboutUtil.class.getClassLoader();
        if (loader == null) loader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> urls = loader.getResources("META-INF/build.txt");
        List<String> result = new ArrayList<String>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String jar = url.toString();
            int i = jar.indexOf(".jar");
            int i0 = jar.lastIndexOf("/", i - 1);
            String name;
            if (i != -1) {
                name = jar.substring(i0 + 1, i + 4);
            } else {
                name = jar.substring(6);
            }
            Properties props = new Properties();
            props.load(url.openStream());
            String cvsTagName = props.getProperty("build.tag");
            String version;
            if (cvsTagName == null || cvsTagName.length() <= 9) {
                version = "untagged_version";
            } else {
                version = cvsTagName.substring(6, cvsTagName.length() - 2);
            }
            result.add(name + ": " + version + "(" + props.getProperty("build.timestamp") + " " + props.getProperty("build.user.name") + ")");
        }
        return result;
    }
