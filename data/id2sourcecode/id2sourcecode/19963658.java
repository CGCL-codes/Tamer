    @Test
    public void testAppend() throws Exception {
        File f = new File(Thread.currentThread().getContextClassLoader().getResource("yarfraw/rss2sample.xml").toURI());
        File copy = new File("testTmpOutput/coverage/testAppend.xml");
        FileUtils.copyFile(f, copy);
        FeedAppender a = new FeedAppender(copy);
        a.setNumItemToKeep(3);
        List<ItemEntry> items = yarfraw.rss20.BuilderTest.buildChannel().getItems();
        a.appendAllItemsToBeginning(items.get(0));
        FeedReader r = new FeedReader(copy);
        assertEquals(3, r.readChannel().getItems().size());
        a.appendAllItemsToBeginning(items);
        assertEquals(3, r.readChannel().getItems().size());
        a.setItem(2, items.get(0));
        assertEquals("item not set correctly", r.readChannel().getItems().get(2), items.get(0));
        a.appendAllItemsToEnd(items);
        ChannelFeed c = r.readChannel();
        assertEquals("item not added correctly", c.getItems().get(2), items.get(0));
    }
