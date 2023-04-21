    @Nonnull
    public static OutputStream getBestSuitableOutputStream(@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) throws IOException {
        final String sAcceptEncoding = aHttpRequest.getHeader("Accept-Encoding");
        if (sAcceptEncoding.contains("gzip")) {
            aHttpResponse.setHeader("Content-Encoding", "gzip");
            return new GZIPOutputStream(aHttpResponse.getOutputStream());
        }
        if (sAcceptEncoding.contains("deflate")) {
            aHttpResponse.setHeader("Content-Encoding", "deflate");
            final ZipOutputStream aOS = new ZipOutputStream(aHttpResponse.getOutputStream());
            aOS.putNextEntry(new ZipEntry("dummy name"));
            return aOS;
        }
        return aHttpResponse.getOutputStream();
    }
