    private void writeCssHtml(Object write) throws IOException {
        Reader in = new BufferedReader(new InputStreamReader(HtmlDiffReportWriter.class.getResourceAsStream("HtmlDiffReportWriter.css"), "UTF-8"));
        int c;
        while ((c = in.read()) != -1) out.write(c);
    }
