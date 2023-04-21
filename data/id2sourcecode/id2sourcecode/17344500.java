    public void testGetContentInputStream() {
        try {
            URL url = new URL("http://www.morganmckinley.ie/job/897859/finance-manager-cork");
            InputStream in = url.openStream();
            Content c = provider.getContent(in);
            assertNotNull(c);
            assertEquals("Job Offering", c.getType());
            assertEquals("ie.morganmckinley.offerings", c.getProvider());
            assertEquals(43, c.getAttributes().size());
            assertEquals("Title", c.getAttributes().iterator().next().getName());
        } catch (MalformedURLException e) {
            fail("Malformed URL - " + e.getMessage());
        } catch (IOException e) {
            fail("Couldn't read source - " + e.getMessage());
        }
    }
