    public static void updateNewspaper(Newspaper newspaper) throws MalformedURLException, RssParserException, IOException, Exception {
        Rss rss = parser.parse(new URL(newspaper.getAddress()));
        newspaper.setName(rss.getChannel().getTitle().toString());
        Collection items = rss.getChannel().getItems();
        if (items != null && !items.isEmpty()) {
            for (Iterator i = items.iterator(); i.hasNext(); ) {
                Item item = (Item) i.next();
                Headline headline = new Headline(item.getTitle().toString(), item.getLink().toString(), item.getDescription().toString());
                newspaper.getHeadlines().getHeadlines().add(headline);
            }
        }
    }
