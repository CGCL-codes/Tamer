    InputStream createTokenStream(String url, FactoryData data) throws FileNotFoundException, IOException {
        fileSource = null;
        URL urlAttempt = null;
        data.setGzip(false);
        try {
            urlAttempt = new URL(url);
            PushbackInputStream test = new PushbackInputStream(urlAttempt.openStream());
            int testchar = test.read();
            test.unread(testchar);
            if (testchar == 0x1f) {
                data.setGzip(true);
                return (new BufferedInputStream(new GZIPInputStream(test)));
            } else {
                return (new BufferedInputStream(test));
            }
        } catch (Exception e) {
            fileSource = new File(url);
            PushbackInputStream test = new PushbackInputStream(new FileInputStream(fileSource));
            int testchar = test.read();
            test.unread(testchar);
            if (testchar == 0x1f) {
                data.setGzip(true);
                return (new BufferedInputStream(new GZIPInputStream(test)));
            } else {
                return (new BufferedInputStream(test));
            }
        }
    }
