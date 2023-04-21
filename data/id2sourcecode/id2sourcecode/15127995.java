    protected void processResponse(String redirectURL, InputStream response, int statusCode, String contentType, String contentEncoding) throws IOException, SAXException {
        boolean ignoreContent = shouldIgnoreContent(statusCode);
        boolean ignoreThisContentType = ContentAction.IGNORE == retrieveContentAction(contentType);
        if (!ignoreContent && !ignoreThisContentType) {
            if (response != null) {
                InputStream stream = getInputStreamFactory().getInputStream(response, contentEncoding);
                PushbackInputStream pbis = new PushbackInputStream(stream);
                if (findStartDelimiter('<', pbis)) {
                    consumeResponse(redirectURL, pbis, contentType);
                }
            }
        } else {
            WebDriverResponse webdriver = retrieveWebDriverResponse();
            boolean saveIgnoredContent = ignoreContent || (ignoreThisContentType && configuration.isIgnoreContentEnabled());
            if (webdriver != null && saveIgnoredContent && response != null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int readBytes = response.read(buffer);
                while (readBytes != -1) {
                    out.write(buffer, 0, readBytes);
                    readBytes = response.read(buffer);
                }
                webdriver.setIgnoredContent(new ByteArrayInputStream(out.toByteArray()));
            }
        }
    }
