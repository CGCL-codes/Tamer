    @SuppressWarnings("unchecked")
    public static Collection<String> inspect(Bundle bundle, BundleVistor visitor) {
        ClassReader classReader;
        Enumeration<URL> urls = bundle.findEntries("/", "*.class", true);
        if (urls == null) return Collections.EMPTY_LIST;
        URL url;
        InputStream in = null;
        while (urls.hasMoreElements()) {
            try {
                url = urls.nextElement();
                in = url.openStream();
                classReader = new ClassReader(in);
                classReader.accept(visitor, ClassReader.SKIP_DEBUG);
            } catch (Exception e) {
            } finally {
                try {
                    if (in != null) in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return visitor.getResults();
    }
