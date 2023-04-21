    public void process(HttpServletResponse response, String urlString, String rect) throws RequestFailureException, IOException {
        Rectangle sourceRegion = validRegion(rect);
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        String contentType = con.getContentType();
        String mimeType = contentType == null ? "" : RequestUtils.asMimeType(contentType);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Specified image mimeType is '" + mimeType + "'");
        }
        ImageReader reader = getImageReader(url, mimeType);
        InputStream is = null;
        OutputStream os = null;
        ImageWriter writer = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Using image reader " + reader);
            }
            if (mimeType.isEmpty()) {
                mimeType = reader.getOriginatingProvider().getMIMETypes()[0];
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Inferred image mimeType is '" + mimeType + "'");
                }
            }
            is = con.getInputStream();
            reader.setInput(new MemoryCacheImageInputStream(is));
            writer = ImageIO.getImageWriter(reader);
            RenderedImage renderedImage = renderThumbnail(sourceRegion, reader);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(contentType == null ? mimeType : contentType);
            os = response.getOutputStream();
            MemoryCacheImageOutputStream mcos = new MemoryCacheImageOutputStream(os);
            writer.setOutput(mcos);
            writer.write(renderedImage);
            mcos.close();
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
            reader.dispose();
            if (writer != null) {
                writer.dispose();
            }
        }
    }
