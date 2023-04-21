    public static void downloadFile(final HttpServletResponse response, final InputStream input, final String fileName, String contentType, final boolean attachment) throws IOException {
        BufferedOutputStream output = null;
        try {
            int contentLength = input.available();
            final String disposition = attachment ? "attachment" : "inline";
            if (contentType == null) {
                contentType = URLConnection.guessContentTypeFromName(fileName);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
            }
            response.reset();
            response.setContentLength(contentLength);
            response.setContentType(contentType);
            response.setHeader("Content-disposition", disposition + "; filename=\"" + fileName + "\"");
            output = new BufferedOutputStream(response.getOutputStream());
            while (contentLength-- > 0) {
                output.write(input.read());
            }
            output.flush();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException e) {
                    final String message = "Closing HttpServletResponse#getOutputStream() failed. " + e;
                    log.error(message);
                }
            }
        }
    }
