    public void testLoadStaticPageFomFile() throws Exception {
        File source = new File("test", "1152083300843.xml");
        File destination = new File(blog.getRoot(), "pages/1152083300843");
        destination.mkdirs();
        FileUtils.copyFile(source, new File(destination, "1152083300843.xml"));
        StaticPage page = dao.loadStaticPage(blog, "1152083300843");
        assertEquals("Static page title", page.getTitle());
        assertEquals("Static page subtitle", page.getSubtitle());
        assertEquals("<p>Static page body.</p>", page.getBody());
        assertEquals("some tags", page.getTags());
        assertEquals(1152083300843L, page.getDate().getTime());
        assertEquals("http://pebble.sourceforge.net", page.getOriginalPermalink());
    }
