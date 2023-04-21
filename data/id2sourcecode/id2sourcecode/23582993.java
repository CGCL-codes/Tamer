    public static void main(String args[]) throws MalformedURLException, IOException, FeedException {
        LoadSpeedChecker chkr = new LoadSpeedChecker();
        FeedAnalyst analyst = new FeedAnalyst();
        Iterator iter = analyst.getEntryIterator(new URL("http://rss.allblog.net/AllPosts.xml"));
        while (iter.hasNext()) {
            SyndEntry entry = (SyndEntry) iter.next();
            System.out.print(entry.getTitle() + ": ");
            URL url = new URL(SearchUtil.getSource(entry.getLink()));
            System.out.println(": " + url);
            System.out.println("(" + chkr.doCheck(url) + "ms)");
            InputStream is = url.openStream();
            String contents = SearchUtil.getContents(is);
            int fcnt = SearchUtil.countMatches(contents, "<meta http-equiv=");
            if (fcnt > 0) {
                System.out.println("found: " + fcnt);
            }
        }
    }
