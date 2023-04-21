    public void testRelativeURL() throws Exception {
        final URL url1 = this.getClass().getResource("x1.txt");
        if (url1 == null) {
            fail("url x1.txt was null");
        } else {
            System.out.println("testRelativeURL: url1=" + url1);
            copy(url1.openStream(), null);
            final URL url2 = new URL(url1, "x2.txt");
            if (url2 == null) {
                fail("testRelativeURL: url x2.txt was null relative to url1");
            } else {
                System.out.println("url2=" + url2);
                copy(url2.openStream(), null);
            }
        }
    }
