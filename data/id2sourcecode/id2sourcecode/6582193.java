    public void testLearningRomeToMixi() throws Exception {
        SyndFeedInput input = new SyndFeedInput();
        String url = "http://mixi.jp/atom/updates/r=1/member_id=1090863";
        URL url2 = new URL(url);
        URLConnection con = url2.openConnection();
        assertTrue(con instanceof HttpURLConnection);
        HttpURLConnection httpCon = (HttpURLConnection) con;
        httpCon.setRequestProperty("X-WSSE", getWsseHeaderValue("kompiro@hotmail.com", "fopcc17m"));
        SyndFeed feed = input.build(new XmlReader(httpCon));
        List<?> entries = feed.getEntries();
        assertFalse(0 == entries.size());
        for (Object entry : entries) {
            System.out.println(entry);
        }
    }
