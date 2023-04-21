    private void downloadFile(HttpResponse response, OutputStream os) throws IOException {
        final InputStream is = response.getEntity().getContent();
        long size = response.getEntity().getContentLength();
        BufferedInputStream bis = new BufferedInputStream(is);
        final byte[] buffer = new byte[1024 * 1024];
        long position = 0;
        while (position < size) {
            final int read = bis.read(buffer, 0, buffer.length);
            if (read <= 0) break;
            os.write(buffer, 0, read);
            os.flush();
            position += read;
        }
        is.close();
    }
