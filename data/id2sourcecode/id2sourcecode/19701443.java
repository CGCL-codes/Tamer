    protected void cleanHtmlCss(InlineStringReader reader, InlineStringWriter writer, boolean withinHtml) throws IOException, CleanerException {
        getCssCleaner().cleanHtmlCss(reader, writer, withinHtml);
    }
