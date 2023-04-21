    public void testURLConnection() throws Exception {
        InputStream dataIS = new ReplayInputStream(new ByteArrayInputStream("My Test Data".getBytes()));
        TestURLRegistry.register("replay", dataIS);
        URL url = new URL("testurl:replay");
        InputStream in = url.openStream();
        assertSame(dataIS, in);
        byte[] dta = new byte[20];
        assertEquals(12, in.read(dta));
        assertEquals("My Test Data", new String(dta, 0, 12));
        assertEquals(12, in.read(dta));
        assertEquals("My Test Data", new String(dta, 0, 12));
    }
