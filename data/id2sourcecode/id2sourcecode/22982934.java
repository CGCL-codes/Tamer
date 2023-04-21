    private static void addResources(List<String> result, URL url) throws IOException {
        Properties props = new Properties();
        InputStream in = url.openStream();
        props.load(in);
        in.close();
        String resources = props.getProperty("resources");
        StringTokenizer st = new StringTokenizer(resources, ";");
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
    }
