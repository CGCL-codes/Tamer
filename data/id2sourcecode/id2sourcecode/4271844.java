    @Override
    public void sendRespond(HttpServletResponse response) throws IOException {
        final int totalLen = getContentLength();
        response.setContentType(getMimeType());
        response.setContentLength(totalLen);
        final String vid = getVersionId();
        if ((vid != null) && (vid.length() > 0)) response.setHeader(HEADER_JNLP_VERSION, getVersionId());
        final long lastMod = getLastModified();
        if (lastMod != 0L) response.setDateHeader(HEADER_LASTMOD, lastMod);
        final String fileName = getFileName();
        if ((fileName != null) && (fileName.length() > 0)) {
            if (fileName.endsWith(JnlpResource.PACK_GZ_SUFFIX)) response.setHeader(CONTENT_ENCODING, PACK200_GZIP_ENCODING); else if (fileName.endsWith(JnlpResource.GZ_SUFFIX)) response.setHeader(CONTENT_ENCODING, GZIP_ENCODING); else response.setHeader(CONTENT_ENCODING, null);
        }
        final String argVal = _log.isDebugLevel() ? getArgString() : null;
        if (_log.isDebugLevel()) _log.debug("sendRespond(" + fileName + ") start " + argVal);
        final OutputStream out = getOutputStream(response);
        InputStream in = null;
        int totalRead = 0;
        final long cpyStart = System.currentTimeMillis();
        try {
            if (null == (in = getContent())) throw new EOFException("No content " + InputStream.class.getSimpleName() + " created");
            final byte[] bytes = new byte[DATA_COPY_SIZE];
            for (int read = in.read(bytes); (read != (-1)) && (totalRead < totalLen); read = in.read(bytes)) {
                if (read > 0) {
                    out.write(bytes, 0, read);
                    totalRead += read;
                } else if (read == (-1)) break;
                if (_log.isTraceLevel()) _log.trace("sendRespond(" + fileName + ") copied " + totalRead + " out of " + totalLen);
            }
            final long cpyEnd = System.currentTimeMillis(), cpyDuration = cpyEnd - cpyStart;
            if (_log.isDebugLevel()) _log.debug("sendRespond(" + fileName + ") end (" + cpyDuration + " msec.) " + argVal);
        } catch (IOException ioe) {
            final long cpyEnd = System.currentTimeMillis(), cpyDuration = cpyEnd - cpyStart;
            _log.warn("sendRespond(" + fileName + ") " + ioe.getClass().getName() + " after copying " + totalRead + " bytes in " + cpyDuration + " msec.: " + ioe.getMessage(), ioe);
            throw ioe;
        } finally {
            if (in != null) in.close();
        }
    }
