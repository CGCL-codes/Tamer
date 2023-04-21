    @Test
    public void chartApiTest() throws IOException {
        URL url = new URL("http://chart.apis.google.com/chart?cht=p3&chd=t:60,40&chs=250x100&chl=Hello|World");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Assert.assertEquals("Wrong status code: " + conn.getResponseCode(), 200, conn.getResponseCode());
        Assert.assertEquals("Wrong data format (should be png)", "image/png", conn.getContentType());
    }
