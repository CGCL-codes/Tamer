    public void testWithPrettyPrintFormatMerged() throws Exception {
        String outText = readwriteText(OutputFormat.createPrettyPrint(), true);
        if (VERBOSE) {
            log("Text output is [");
            log(outText);
            log("]. Done");
        }
        assertTrue("Output text contains \"&amp;\"", outText.lastIndexOf("&amp;") >= 0);
        assertTrue("Output text contains \"&lt;\"", outText.lastIndexOf("&lt;") >= 0);
    }
