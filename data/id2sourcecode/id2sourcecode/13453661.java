    public void test_getContentType() throws IOException {
        assertTrue("getContentType failed: " + uc.getContentType(), uc.getContentType().contains("text/html"));
        File resources = Support_Resources.createTempFolder();
        Support_Resources.copyFile(resources, null, "Harmony.GIF");
        URL url = new URL("file:/" + resources.toString() + "/Harmony.GIF");
        URLConnection conn = url.openConnection();
        assertEquals("type not GIF", "image/gif", conn.getContentType());
    }
