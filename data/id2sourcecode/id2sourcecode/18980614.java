    public static BaseMetaobjectProtocol[] discoverBaseMetaobjectProtocols(ClassLoader cl) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<BaseMetaobjectProtocol> protocols = new LinkedList<BaseMetaobjectProtocol>();
        Enumeration<URL> urls;
        if (cl == null) {
            urls = ClassLoader.getSystemResources(RESOURCE_PATH);
            cl = ClassLoader.getSystemClassLoader();
        } else {
            urls = cl.getResources(RESOURCE_PATH);
        }
        for (; urls.hasMoreElements(); ) {
            URL url = urls.nextElement();
            InputStream in = url.openStream();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                for (; ; ) {
                    String className = r.readLine();
                    if (className == null) {
                        break;
                    }
                    className = className.trim();
                    if ("".equals(className) || className.startsWith("#")) {
                        continue;
                    }
                    protocols.add((BaseMetaobjectProtocol) Class.forName(className.toString(), true, cl).newInstance());
                }
            } finally {
                in.close();
            }
        }
        return protocols.toArray(new BaseMetaobjectProtocol[protocols.size()]);
    }
