    public void testResourceURL() throws IOException, MalformedURLException {
        count++;
        if (shouldSkip()) return;
        String image = "/images/button.mail.gif";
        String resource = "onejar:" + image;
        System.out.println("testResourceURL(): Opening onejar resource using new URL(" + resource + ")");
        URL url = new URL(resource);
        InputStream is = url.openStream();
        System.out.println("testResourceURL(): Opened: " + url);
        if (is == null) {
            fail("testResourceURL(): Error: Huh? Should find " + resource + " as a resource");
        } else {
            System.out.println("testResourceURL(): OK.");
        }
        System.out.println("testResourceURL(): opening using getResource(" + image + ")");
        url = Test.class.getResource(image);
        System.out.println("testResourceURL(): Opened: " + url);
        if (url == null) {
            fail("testResourceURL(): Error: Huh? Should find " + resource + " using getResource()");
        } else {
            is = url.openStream();
            if (is == null) {
                fail("testResourceURL(): Error: Huh? Should find " + resource + " as a resource");
            } else {
                System.out.println("testResourceURL(): OK.");
            }
        }
    }
