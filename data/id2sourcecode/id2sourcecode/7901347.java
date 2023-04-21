    public void testSameBytes() throws IOException {
        String link;
        URL url;
        URLConnection connection1;
        URLConnection connection2;
        BufferedInputStream in;
        int b1;
        int b2;
        Stream stream;
        int index;
        link = "http://htmlparser.sourceforge.net";
        try {
            url = new URL(link);
            connection1 = url.openConnection();
            connection1.connect();
            in = new BufferedInputStream(connection1.getInputStream());
            connection2 = url.openConnection();
            connection2.connect();
            stream = new Stream(connection2.getInputStream());
            index = 0;
            while (-1 != (b1 = in.read())) {
                b2 = stream.read();
                if (b1 != b2) fail("bytes differ at position " + index + ", expected " + b1 + ", actual " + b2);
                index++;
            }
            b2 = stream.read();
            stream.close();
            in.close();
            assertTrue("extra bytes", b2 == -1);
        } catch (MalformedURLException murle) {
            fail("bad url " + link);
        }
    }
