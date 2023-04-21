    protected final void readInput(PipelineContext pipelineContext, ExternalContext.Response response, ProcessorInput input, Object _config, OutputStream outputStream) {
        Config config = (Config) _config;
        String encoding = getEncoding(config, null, DEFAULT_ENCODING);
        if (response != null) {
            String contentType = getContentType(config, null, getDefaultContentType());
            if (contentType != null) response.setContentType(contentType + "; charset=" + encoding);
        }
        try {
            Writer writer = new OutputStreamWriter(outputStream, encoding);
            readInput(pipelineContext, input, config, writer);
        } catch (UnsupportedEncodingException e) {
            throw new OXFException(e);
        }
    }
