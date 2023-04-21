    public FileEntity requestToFileEntity(final HttpUriRequest request) throws IOException {
        final HttpResponse response = httpClient.execute(request);
        final String mimeType = response.getEntity().getContentType().getValue();
        final byte[] bytes = IOUtil.toByteArray(response.getEntity().getContent());
        return new FileEntity(mimeType, bytes, null);
    }
