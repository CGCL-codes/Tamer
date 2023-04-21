    public void testResourceRelativeURL() throws IOException, MalformedURLException {
        count++;
        String image = "button.mail.1.gif";
        System.out.println("testResourceRelativeURL(): opening using getResource(" + image + ")");
        URL url = Test.class.getResource(image);
        System.out.println("testResourceRelativeURL(): Opened: " + url);
        if (url == null) {
            fail("testResourceRelativeURL(): Error: Huh? Should find " + image + " using getResource()");
        } else {
            InputStream is = url.openStream();
            if (is == null) {
                fail("testResourceRelativeURL(): Error: Huh? Should find " + image + " as a resource");
            } else {
                System.out.println("testResourceRelativeURL(): OK.");
            }
        }
    }
