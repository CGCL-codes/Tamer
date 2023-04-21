    private synchronized void crawlHttp(final String url) throws Exception {
        final URL getUrl = new URL(url);
        URLConnection conn = getUrl.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        String line;
        final BufferedReader rdResponse = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuffer content = new StringBuffer();
        while (null != (line = rdResponse.readLine())) {
            content.append(line);
        }
        rdResponse.close();
        visitedPages.add(new URL(url).getPath());
        final Source source = new Source(content.toString());
        try {
            final List elements = source.findAllStartTags("A");
            if (null != elements) {
                for (Iterator i = elements.iterator(); i.hasNext(); ) {
                    final StartTag startTag = (StartTag) i.next();
                    final Attributes attributes = startTag.getAttributes();
                    final Attribute hrefAttribute = attributes.get("href");
                    if (null != hrefAttribute) {
                        final URL oldUrl = new URL(url);
                        final URL newUrl = new URL(oldUrl, hrefAttribute.getValue());
                        if (newUrl.getHost().equalsIgnoreCase(oldUrl.getHost()) && !visitedPages.contains(newUrl.getPath())) {
                            crawlHttp(newUrl.toString());
                        }
                    }
                }
            }
            if (new URL(url).getPath().endsWith("html") || new URL(url).getPath().endsWith("htm") || new URL(url).getPath().endsWith("txt") || new URL(url).getPath().endsWith("jsp")) {
                indexHTML(content.toString(), url);
            } else if (new URL(url).getPath().endsWith("pdf")) {
                indexPDF(LucenePDFDocument.getDocument(new URL(url)));
            } else if (new URL(url).getPath().endsWith("doc")) {
                conn = getUrl.openConnection();
                final WordDocument d = new WordDocument(conn.getInputStream());
                final StringWriter wr = new StringWriter();
                d.writeAllText(wr);
                indexDoc(wr.toString(), url);
            }
        } catch (Exception e) {
            e.toString();
        }
    }
