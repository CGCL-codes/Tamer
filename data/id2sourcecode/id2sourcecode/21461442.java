    public void start(org.orbeon.oxf.pipeline.api.PipelineContext context) {
        ExternalContext externalContext = (ExternalContext) context.getAttribute(org.orbeon.oxf.pipeline.api.PipelineContext.EXTERNAL_CONTEXT);
        ExternalContext.Response response = externalContext.getResponse();
        MimeTypeConfig mimeTypeConfig = (MimeTypeConfig) readCacheInputAsObject(context, getInputByName(MIMETYPE_INPUT), new CacheableInputReader() {

            public Object read(PipelineContext context, ProcessorInput input) {
                MimeTypesContentHandler ch = new MimeTypesContentHandler();
                readInputAsSAX(context, input, ch);
                return ch.getMimeTypes();
            }
        });
        try {
            Node configNode = readCacheInputAsDOM(context, INPUT_CONFIG);
            String urlString = XPathUtils.selectStringValueNormalize(configNode, "url");
            if (urlString == null) {
                urlString = XPathUtils.selectStringValueNormalize(configNode, "path");
                if (urlString == null) throw new OXFException("Missing configuration.");
                urlString = "oxf:" + urlString;
            }
            URLConnection urlConnection = null;
            InputStream urlConnectionInputStream = null;
            try {
                try {
                    URL newURL = URLFactory.createURL(urlString);
                    urlConnection = newURL.openConnection();
                    urlConnectionInputStream = urlConnection.getInputStream();
                    long lastModified = NetUtils.getLastModified(urlConnection);
                    response.setCaching(lastModified, false, false);
                    if (!response.checkIfModifiedSince(lastModified, false)) {
                        response.setStatus(ExternalContext.SC_NOT_MODIFIED);
                        return;
                    }
                    String contentType = mimeTypeConfig.getMimeType(urlString);
                    if (contentType != null) response.setContentType(contentType);
                    int length = urlConnection.getContentLength();
                    if (length > 0) response.setContentLength(length);
                } catch (IOException e) {
                    response.setStatus(ExternalContext.SC_NOT_FOUND);
                    return;
                }
                NetUtils.copyStream(urlConnectionInputStream, response.getOutputStream());
            } finally {
                if (urlConnection != null && "file".equalsIgnoreCase(urlConnection.getURL().getProtocol())) {
                    if (urlConnectionInputStream != null) urlConnectionInputStream.close();
                }
            }
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }
