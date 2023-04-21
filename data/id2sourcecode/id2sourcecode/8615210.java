    private void verifyPage(String host, String path) throws Exception {
        StringBuilder urlString = new StringBuilder("http://");
        urlString.append(host);
        urlString.append(":");
        urlString.append(port);
        urlString.append(context);
        urlString.append(path);
        logger.info("Executing page : " + urlString);
        URL url = new URL(urlString.toString());
        String content = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
        assertNotNull(content);
        verifyContent(new ByteArrayInputStream(content.getBytes()));
    }
