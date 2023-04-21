    @Test
    public void testAppend2() throws Exception {
        File f = new File(Thread.currentThread().getContextClassLoader().getResource("yarfraw/digg.xml").toURI());
        File copy = new File("testTmpOutput/rss20/testAppend.xml");
        List<ItemEntry> items = BuilderTest.buildChannel().getItems();
        FeedWriter w = new FeedWriter(copy);
        w.writeChannel(new FeedReader(f).readChannel());
        FeedAppender a = new FeedAppender(copy);
        a.setNumItemToKeep(10);
        a.appendAllItemsToBeginning(items.get(0));
        FeedReader r = new FeedReader(copy);
        assertEquals(10, r.readChannel().getItems().size());
        a.appendAllItemsToBeginning(items);
        assertEquals(10, r.readChannel().getItems().size());
        a.setItem(0, BuilderTest.buildChannel().getItems().get(1));
        assertEquals("item not set correctly", r.readChannel().getItems().get(0), items.get(1));
        a.appendAllItemsToBeginning(BuilderTest.buildChannel().getItems().get(1));
        assertEquals("item not added correctly", r.readChannel().getItems().get(0), items.get(1));
    }
