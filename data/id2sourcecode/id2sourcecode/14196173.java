    public static String[] loadAllPropertiesFromClassLoader(Properties properties, String... resourceNames) throws IOException {
        List successLoadProperties = new ArrayList();
        for (String resourceName : resourceNames) {
            Enumeration urls = GeneratorProperties.class.getClassLoader().getResources(resourceName);
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                successLoadProperties.add(url.getFile());
                InputStream input = null;
                try {
                    URLConnection con = url.openConnection();
                    con.setUseCaches(false);
                    input = con.getInputStream();
                    if (resourceName.endsWith(".xml")) {
                        properties.loadFromXML(input);
                    } else {
                        properties.load(input);
                    }
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }
        }
        return (String[]) successLoadProperties.toArray(new String[0]);
    }
