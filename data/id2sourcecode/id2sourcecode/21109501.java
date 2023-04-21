    public void testWithoutFormatMerged() throws Exception {
        String outText = readwriteText(null, true);
        if (VERBOSE) {
            log("Text output is [");
            log(outText);
            log("]. Done");
        }
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }
