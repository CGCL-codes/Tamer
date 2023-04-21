    public void write(Writer writer, SolrQueryRequest request, SolrQueryResponse response) throws IOException {
        Object obj = response.getValues().get(CONTENT);
        if (obj != null && (obj instanceof ContentStream)) {
            ContentStream content = (ContentStream) obj;
            Reader reader = content.getReader();
            try {
                IOUtils.copy(reader, writer);
            } finally {
                reader.close();
            }
        } else {
            getBaseWriter(request).write(writer, request, response);
        }
    }
