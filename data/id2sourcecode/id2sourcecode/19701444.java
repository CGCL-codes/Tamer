    protected void cleanHtmlJavascript(InlineStringReader reader, InlineStringWriter writer, boolean withinHtml) throws IOException, CleanerException {
        getJavascriptCleaner().cleanJavascriptBlock(reader, writer, withinHtml);
    }
