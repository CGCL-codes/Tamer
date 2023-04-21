    @Test
    public void chartApiBadRequestTest() throws IOException {
        URL url = new URL("http://chart.apis.google.com/chart?cht=p3d&chd=t:60,40&chs=250x100&chl=Hello|World");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Assert.assertEquals("Wrong status code: " + conn.getResponseCode(), 400, conn.getResponseCode());
    }
