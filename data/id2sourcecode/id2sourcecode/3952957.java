    public void convert(InputStream in, OutputStream out, ConversionParameters params) throws ConverterException, IOException {
        if (params.getSourceCharset() == null || params.getTargetCharset() == null) {
            throw new ConverterException("Missing charset {source=" + params.getSourceCharset() + ", target=" + params.getTargetCharset() + "}.");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating reader(" + params.getSourceCharset() + ") and writer(" + params.getTargetCharset() + ").");
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        reader = new BufferedReader(new InputStreamReader(in, CharsetUtil.forName(params.getSourceCharset())));
        writer = new BufferedWriter(new OutputStreamWriter(out, CharsetUtil.forName(params.getTargetCharset())));
        convert(reader, writer, params);
        try {
            writer.flush();
        } catch (Exception e) {
        }
    }
