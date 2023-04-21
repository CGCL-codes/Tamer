        @Override
        public HttpResponse execute(final HttpRequest request, final HttpClientConnection conn, final HttpContext context) throws IOException, HttpException {
            Object marker = context.getAttribute(MARKER);
            if (marker == null) {
                context.setAttribute(MARKER, Boolean.TRUE);
                throw new IOException(failureMsg);
            }
            return super.execute(request, conn, context);
        }
