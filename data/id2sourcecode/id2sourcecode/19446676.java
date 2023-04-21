    private void handleResourceRequest(PhaseEvent event, String resource, String contentType) {
        LOG.info("Debug: Trying to load: " + resource);
        URL url = null;
        url = PhaseListener.class.getResource(resource);
        LOG.info("Debug: url = " + url);
        URLConnection conn = null;
        InputStream stream = null;
        BufferedReader bufReader = null;
        HttpServletResponse response = (HttpServletResponse) event.getFacesContext().getExternalContext().getResponse();
        OutputStreamWriter outWriter = null;
        String curLine = null;
        try {
            outWriter = new OutputStreamWriter(response.getOutputStream(), response.getCharacterEncoding());
            conn = url.openConnection();
            conn.setUseCaches(false);
            stream = conn.getInputStream();
            bufReader = new BufferedReader(new InputStreamReader(stream));
            response.setContentType(contentType);
            response.setStatus(200);
            while (null != (curLine = bufReader.readLine())) {
                outWriter.write(curLine + "\n");
            }
            outWriter.flush();
            event.getFacesContext().responseComplete();
        } catch (IOException e) {
            LOG.debug("Can't load resource:" + url.toExternalForm());
        } finally {
            IOUtils.closeQuietly(outWriter);
            IOUtils.closeQuietly(bufReader);
            IOUtils.closeQuietly(stream);
        }
    }
