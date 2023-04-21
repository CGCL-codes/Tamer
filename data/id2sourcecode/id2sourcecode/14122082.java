    public void testGetContentInputStream() {
        try {
            URL url = new URL("http://www.zolltarifnummern.de/2010_de/10011000.html");
            InputStream in = url.openStream();
            Content c = provider.getContent(in);
            assertNotNull(c);
            assertEquals("Custom Tariff", c.getType());
            assertEquals("com.iqser.plugin.web.custom", c.getProvider());
            assertEquals(5, c.getAttributes().size());
            assertEquals("TARIC 10011000", c.getAttributes().iterator().next().getName());
        } catch (MalformedURLException e) {
            fail("Malformed URL - " + e.getMessage());
        } catch (IOException e) {
            fail("Couldn't read source - " + e.getMessage());
        }
    }
