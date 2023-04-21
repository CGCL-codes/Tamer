    public void test_openConnection_SelectorCalled() throws MalformedURLException {
        URL httpUrl = new URL("http://" + Support_Configuration.ProxyServerTestHost + "/cgi-bin/test.pl");
        URL ftpUrl = new URL("ftp://" + Support_Configuration.FTPTestAddress + "/nettest.txt");
        URL[] urlList = { httpUrl, ftpUrl };
        ProxySelector originalSelector = ProxySelector.getDefault();
        ProxySelector.setDefault(new MockProxySelector());
        try {
            for (int i = 0; i < urlList.length; ++i) {
                try {
                    isSelectCalled = false;
                    URLConnection conn = urlList[i].openConnection();
                    conn.getInputStream();
                } catch (Exception e) {
                }
                assertTrue("openConnection should call ProxySelector.select(), url = " + urlList[i], isSelectCalled);
            }
        } finally {
            ProxySelector.setDefault(originalSelector);
        }
    }
