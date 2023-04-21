    public void testMarkResetThreaded() throws IOException {
        String link;
        ArrayList bytes1;
        ArrayList bytes2;
        URL url;
        URLConnection connection;
        Stream stream;
        int b;
        int index;
        link = "http://htmlparser.sourceforge.net/javadoc_1_3/overview-summary.html";
        bytes1 = new ArrayList();
        bytes2 = new ArrayList();
        try {
            url = new URL(link);
            connection = url.openConnection();
            connection.connect();
            stream = new Stream(connection.getInputStream());
            (new Thread(stream)).start();
            assertTrue("mark not supported", stream.markSupported());
            for (int i = 0; i < 1000; i++) {
                b = stream.read();
                bytes1.add(new Byte((byte) b));
            }
            stream.reset();
            for (int i = 0; i < 1000; i++) {
                b = stream.read();
                bytes2.add(new Byte((byte) b));
            }
            index = 0;
            while (index < bytes1.size()) {
                assertEquals("bytes differ at position " + index, bytes1.get(index), bytes2.get(index));
                index++;
            }
            bytes1.clear();
            bytes2.clear();
            stream.mark(1000);
            for (int i = 0; i < 1000; i++) {
                b = stream.read();
                bytes1.add(new Byte((byte) b));
            }
            stream.reset();
            for (int i = 0; i < 1000; i++) {
                b = stream.read();
                bytes2.add(new Byte((byte) b));
            }
            stream.close();
            index = 0;
            while (index < bytes1.size()) {
                assertEquals("bytes differ at position " + (index + 1000), bytes1.get(index), bytes2.get(index));
                index++;
            }
        } catch (MalformedURLException murle) {
            fail("bad url " + link);
        }
    }
