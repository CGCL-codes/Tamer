    public String processFeed(URL url, String outFormat) throws IOException, FeedException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParsingFeedException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "myuseragent");
        SyndFeed feed = input.build(new XmlReader(urlConnection));
        feed.setFeedType(outFormat);
        Iterator entryIter = feed.getEntries().iterator();
        while (entryIter.hasNext()) {
            SyndEntry entry = (SyndEntry) entryIter.next();
            String description = entry.getDescription().getValue().toString();
            String title = entry.getTitle();
            String nohtmlDescription = description.replaceAll("\\<.*?>", "");
            String nohtmlDescription2 = nohtmlDescription.replaceAll("\"", "\'");
            String nohtmlTitle = title.replaceAll("\\<.*?>", "");
            String nohtmlTitle2 = nohtmlTitle.replaceAll("\"", "\'");
            st = con.createStatement();
            st.executeUpdate("REPLACE INTO feeddata(id, storyid, pubdate, url, title, summary) VALUES ((SELECT id FROM feeds WHERE feeds.feedlink = \'" + url + "\'), 0," + "\"" + entry.getPublishedDate() + "\", \"" + entry.getUri() + "\"," + "\"" + nohtmlTitle2 + "\"," + "\"" + nohtmlDescription2 + "\")");
        }
        StringWriter writer = new StringWriter();
        output.output(feed, writer);
        return writer.toString();
    }
