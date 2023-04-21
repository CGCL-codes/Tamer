    public String requestToString(final HttpUriRequest request) throws IOException {
        final HttpResponse response = httpClient.execute(request);
        final byte[] ret = IOUtil.toByteArray(response.getEntity().getContent());
        if (isFirstRequest) {
            charset = parseCharset(response.getEntity().getContentType().getValue());
            if (charset == null) {
                try {
                    charset = parseCharsetFromHtml(new String(ret));
                } catch (final Exception e) {
                }
            }
            isFirstRequest = false;
        }
        return charset == null ? new String(ret) : new String(ret, charset);
    }
