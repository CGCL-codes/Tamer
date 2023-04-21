    public static String readResource(String resource) throws Exception {
        URL url = CreateScriptUtil.class.getClassLoader().getResource(resource);
        if (url == null) {
            throw new IllegalArgumentException("Could not find resource " + resource + ".");
        }
        StringWriter out = new StringWriter();
        Reader in = new InputStreamReader(url.openStream(), "UTF-8");
        try {
            char[] buffer = new char[1024];
            int nread = 0;
            while ((nread = in.read(buffer)) != -1) {
                out.write(buffer, 0, nread);
            }
            return out.toString();
        } finally {
            in.close();
        }
    }
