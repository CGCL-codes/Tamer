    private void sendImage(final File file, final HttpServletResponse response) throws IOException {
        log.debug("file: {}", file);
        response.setContentLength((int) file.length());
        response.setContentType("image/jpeg");
        response.setHeader("Content-Disposition", "inline;filename=" + file.getName());
        final OutputStream httpOut = response.getOutputStream();
        final int len = 8 * 1024;
        final InputStream fileIstream = new FileInputStream(file);
        final InputStream buffIstream = new BufferedInputStream(fileIstream, len);
        final byte[] buf = new byte[len];
        int bytesRead;
        while ((bytesRead = buffIstream.read(buf, 0, len)) != -1) httpOut.write(buf, 0, bytesRead);
        httpOut.flush();
        httpOut.close();
        fileIstream.close();
        buffIstream.close();
    }
