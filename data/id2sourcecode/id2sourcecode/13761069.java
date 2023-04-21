    private void initFeeds(String feedUrls) {
        String[] urls = feedUrls.split(";");
        List entryList = new ArrayList();
        int entry_num = (10 / urls.length) < 0 ? 1 : (10 / urls.length);
        for (int i = 0; i < urls.length; i++) {
            try {
                java.net.URL url = new java.net.URL(urls[i]);
                com.sun.syndication.io.SyndFeedInput input = new com.sun.syndication.io.SyndFeedInput();
                com.sun.syndication.feed.synd.SyndFeed feed = input.build(new java.io.InputStreamReader(url.openStream()));
                int count = (feed.getEntries().size() > entry_num) ? entry_num : feed.getEntries().size();
                for (int j = 0; j < count; j++) {
                    entryList.add(feed.getEntries().get(j));
                }
            } catch (Exception e) {
                log.error("error reading url " + urls[i], e);
            }
        }
        Comparator comp = new Comparator() {

            public int compare(Object o1, Object o2) {
                if (o1 == null || o2 == null) return 0;
                if (((SyndEntry) o1).getPublishedDate() == null || ((SyndEntry) o2).getPublishedDate() == null) return 0;
                return ((SyndEntry) o2).getPublishedDate().compareTo(((SyndEntry) o1).getPublishedDate());
            }

            public boolean equals(Object obj) {
                return false;
            }
        };
        Collections.sort(entryList, comp);
        setEntryList(entryList);
    }
