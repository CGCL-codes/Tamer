    @Override
    protected FileDownloadResult doRequest(final URL pUrl) throws IOException, BoxException {
        final FileDownloadResult r = new FileDownloadResult();
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            final String endUrl = getEndpointURL();
            final URL url = new URL(endUrl);
            final URLConnection conn = url.openConnection();
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bis = new BufferedInputStream(conn.getInputStream());
            final byte[] buf = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = bis.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, bytesRead);
            }
            final String file = getParam(KEY_FILE_NAME);
            final String folder = getParam(KEY_SAVE_FOLDER);
            final File fileOut = new File(folder, file);
            bos = new BufferedOutputStream(new FileOutputStream(fileOut));
            bos.write(baos.toByteArray());
            r.setStatus(FileDownloadResult.SUCCESSFUL_DOWNLOAD);
            r.setXmlResponse(FileDownloadResult.toStatusXml(FileDownloadResult.SUCCESSFUL_DOWNLOAD, "File downloaded to " + fileOut.getAbsolutePath()));
        } finally {
            RestUtils.closeQuietly(bis);
            RestUtils.closeQuietly(bos);
        }
        return r;
    }
