    public static void main(String[] args) throws IOException, URISyntaxException {
        getURLListFromResource("org/servingMathematics/mqat/examples", ".*[.mqat]", false);
        ClassLoader thisCL = TestJAR.class.getClassLoader();
        URLClassLoader thisURLCL = (URLClassLoader) thisCL;
        Enumeration e = thisURLCL.findResources("org/servingMathematics/mqat/examples");
        JarFile jarFile;
        File file;
        File[] files;
        for (; e.hasMoreElements(); ) {
            URL url = (URL) e.nextElement();
            System.out.println(url);
            URI uri = new URI(new URL("jar:file:/C:/Projects/imperial/workspace/mqat/trunk/lib/mqat-examples.jar!/org/servingMathematics/mqat/examples/Digging_hole.xml").toString());
            url = uri.toURL();
            System.out.println(url);
            JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
            jarFile = jarConnection.getJarFile();
            Enumeration ee = jarFile.entries();
            for (; ee.hasMoreElements(); ) {
                JarEntry jarEntry = (JarEntry) ee.nextElement();
                if (!jarEntry.isDirectory()) {
                    System.out.println(jarEntry);
                    String fileURI = url.toString().substring(0, url.toString().lastIndexOf('!') + 1);
                    fileURI += "/" + jarEntry;
                    System.out.println(fileURI);
                }
            }
        }
        URL[] urls = thisURLCL.getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i]);
        }
    }
