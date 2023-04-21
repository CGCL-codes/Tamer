    public void testNewInstanceUnknown() throws Exception {
        FileInputStream in = null;
        try {
            Rss20 rss20 = m_factory.newInstance(new FileInputStream(getWorkDir() + File.separator + "example-rss20-3.xml"));
            assertNotNull(rss20.getChannel());
            assertEquals("Simple Feed Title", rss20.getChannel().getTitle());
            assertEquals("http://archtea.sourceforge.net/", rss20.getChannel().getLink());
            assertEquals("Simple Feed Description", rss20.getChannel().getDescription());
            assertNotNull(rss20.getChannel().getItems());
            assertEquals(1, rss20.getChannel().getItems().length);
            assertEquals("Item title", rss20.getChannel().getItems()[0].getTitle());
            assertEquals("Simple Description in item", rss20.getChannel().getItems()[0].getDescription());
        } finally {
            if (in != null) in.close();
        }
    }
