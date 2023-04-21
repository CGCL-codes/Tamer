    @Override
    public void perform() throws Exception {
        String host = "localhost";
        for (String indexName : indexNames) {
            final StringBuilder urlString = new StringBuilder("http://");
            urlString.append(host);
            urlString.append(":");
            urlString.append(port);
            urlString.append(context);
            urlString.append(IConstants.SEP + SearchServlet.class.getSimpleName());
            String searchString = "quick brown fox jumped over the lazy dog";
            urlString.append("?");
            urlString.append(IConstants.INDEX_NAME);
            urlString.append("=");
            urlString.append(indexName);
            urlString.append("&");
            urlString.append(IConstants.SEARCH_STRINGS);
            urlString.append("=");
            urlString.append(URLEncoder.encode(searchString, IConstants.ENCODING));
            logger.info("Executing servlet : " + urlString);
            double executionsPerSecond = PerformanceTester.execute(new PerformanceTester.APerform() {

                public void execute() throws Exception {
                    URL url = new URL(urlString.toString());
                    String content = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
                    assertNotNull(content);
                }
            }, "Servlet performance test : ", iterations, Boolean.FALSE);
            assertTrue(executionsPerSecond > 3);
        }
    }
