    public static void main(String[] args) throws Exception {
        URL url = new URL("http://jroller.com/rss");
        RSSReader reader = new RSSReader(url);
        Feed feed = reader.parse();
        System.out.println("Feed version: " + feed.getVersion());
        System.out.println("Channel title: " + feed.getChannel().getTitle());
        System.out.println("Channel description: " + feed.getChannel().getDescription());
        LinkedList list = (LinkedList) feed.getChannel().getItems();
        for (int i = 0; i < list.size(); i++) {
            Item item = (Item) list.get(i);
            System.out.println("Item title: " + item.getTitle());
            System.out.println("Item category: " + item.getCategory());
            System.out.println("Item description: " + item.getDescription());
        }
    }
