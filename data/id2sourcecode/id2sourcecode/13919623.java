    public void testClassURL() throws IOException, MalformedURLException {
        count++;
        if (shouldSkip()) return;
        String className = "/com/simontuffs/onejar/example/main/Main.class";
        String resource = "onejar:" + className;
        System.out.println("testClassURL(): Opening onejar resource using new URL(" + resource + ")");
        URL url = new URL(resource);
        InputStream is = url.openStream();
        System.out.println("testClassURL(): Opened: " + url);
        if (is == null) {
            fail("testClassURL(): Error: Huh? Should find " + resource + " as a resource");
        } else {
            System.out.println("testClassURL(): OK.");
        }
        System.out.println("testClassURL(): opening using getResource(" + className + ")");
        url = Test.class.getResource(className);
        System.out.println("testClassURL(): Opened: " + url);
        is = url.openStream();
        if (is == null) {
            fail("testClassURL(): Error: Huh? Should find " + resource + " as a resource");
        } else {
            System.out.println("testClassURL(): OK.");
        }
    }
