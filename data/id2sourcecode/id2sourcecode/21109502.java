    public void testWithCompactFormatMerged() throws Exception {
        String out = readwriteText(OutputFormat.createCompactFormat(), true);
        if (VERBOSE) {
            log("Text output is [");
            log(out);
            log("]. Done");
        }
        assertTrue("Output text contains \"&amp;\"", out.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", out.lastIndexOf("&lt;") >= 0);
    }
