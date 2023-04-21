    public static List<String> getBuildInfos() throws IOException {
        Enumeration<URL> urls = Util.class.getClassLoader().getResources("META-INF/build.txt");
        Enumeration<URL> urls2 = Util.class.getClassLoader().getResources("META-INF/build.txt");
        List<URL> urls3 = new ArrayList();
        while (urls2.hasMoreElements()) {
            urls3.add(urls2.nextElement());
        }
        List<String> result = new ArrayList<String>();
        LinkedHashMap<String, String> abbrevs = new LinkedHashMap<String, String>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String jar = url.toString();
            int i = jar.indexOf(".jar");
            int i0 = jar.lastIndexOf("/", i - 1);
            String name;
            String jarname;
            if (i != -1) {
                name = jar.substring(i0 + 1, i + 4);
                jarname = name.substring(0, name.length() - 4);
            } else {
                name = jar.substring(6);
                jarname = name;
            }
            Properties props = new Properties();
            props.load(url.openStream());
            String svnTag = svnTag(props.getProperty("build.svnurl"), props.getProperty("build.svnrevision"), jarname);
            String tagName = tagName(svnTag, abbrevs);
            result.add(name + ": " + tagName + " (" + props.getProperty("build.timestamp") + " " + props.getProperty("build.user.name") + ")");
        }
        for (Entry<String, String> val : abbrevs.entrySet()) {
            result.add("" + val.getValue() + " " + val.getKey());
        }
        return result;
    }
