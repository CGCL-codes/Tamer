    public void processSourceRequest(BufferedReader reader, PrintWriter writer, String pathInfo) {
        assert reader != null;
        assert writer != null;
        assert pathInfo != null;
        int lastIndex = pathInfo.lastIndexOf(SOURCE_NAME_PREFIX);
        int position = lastIndex + SOURCE_NAME_PREFIX.length();
        String sourceName = pathInfo.substring(position + 1);
        if (!this.registry.hasSource(sourceName)) {
            RequestException ex = RequestException.forSourceNotFound(sourceName);
            logger.error(sourceName);
            throw ex;
        }
        String code = this.registry.getSource(sourceName);
        assert code != null;
        writer.append(code);
    }
