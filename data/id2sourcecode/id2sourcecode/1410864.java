    public void testSameChars() throws IOException {
        String link;
        URL url;
        URLConnection connection1;
        URLConnection connection2;
        InputStreamReader in;
        int c1;
        int c2;
        Source source;
        int index;
        link = "http://htmlparser.sourceforge.net";
        try {
            url = new URL(link);
            connection1 = url.openConnection();
            connection1.connect();
            in = new InputStreamReader(new BufferedInputStream(connection1.getInputStream()), "UTF-8");
            connection2 = url.openConnection();
            connection2.connect();
            source = new InputStreamSource(new Stream(connection2.getInputStream()), "UTF-8");
            index = 0;
            while (-1 != (c1 = in.read())) {
                c2 = source.read();
                if (c1 != c2) fail("characters differ at position " + index + ", expected " + c1 + ", actual " + c2);
                index++;
            }
            c2 = source.read();
            assertTrue("extra characters", -1 == c2);
            source.close();
            in.close();
        } catch (MalformedURLException murle) {
            fail("bad url " + link);
        }
    }
