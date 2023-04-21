    public void testGetContentInputStream() {
        try {
            URL url = new URL("http://www.wlw.de/sse/MainServlet?anzeige=vollanzeige&land=DE&sprache=de&firmaid=278527&klobjid=85340&ccode=310131180163&suchbegriff=Maschinen");
            InputStream in = url.openStream();
            Content c = provider.getContent(in);
            assertNotNull(c);
            assertEquals("Vendor", c.getType());
            assertEquals("com.iqser.plugin.web.wlw", c.getProvider());
            assertEquals(13, c.getAttributes().size());
            assertEquals("Name", c.getAttributes().iterator().next().getName());
        } catch (MalformedURLException e) {
            fail("Malformed URL - " + e.getMessage());
        } catch (IOException e) {
            fail("Couldn't read source - " + e.getMessage());
        }
    }
