    private void renderResource(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        ExternalContext extContext = context.getExternalContext();
        String resourceName = getResourceFromRequest(extContext);
        if (null == resourceName) {
            return;
        }
        URL url = ResourcePhaseListener.class.getResource(resourceName);
        URLConnection conn = null;
        InputStream stream = null;
        BufferedReader bufReader = null;
        Object response = extContext.getResponse();
        HttpServletResponse servletResponse = null;
        OutputStreamWriter outWriter = null;
        String contentType = getContentTypeFromRequest(extContext, resourceName), curLine = null;
        try {
            if (response instanceof HttpServletResponse) {
                servletResponse = (HttpServletResponse) response;
                outWriter = new OutputStreamWriter(servletResponse.getOutputStream(), servletResponse.getCharacterEncoding());
                conn = url.openConnection();
                conn.setUseCaches(false);
                stream = conn.getInputStream();
                bufReader = new BufferedReader(new InputStreamReader(stream));
                servletResponse.setContentType(contentType);
                servletResponse.setStatus(200);
            }
            while (null != (curLine = bufReader.readLine())) {
                outWriter.write(curLine + "\n");
            }
            outWriter.flush();
            outWriter.close();
            event.getFacesContext().responseComplete();
        } catch (Exception e) {
            System.out.println("Exception during resource rendering");
            e.printStackTrace(System.out);
            String message = null;
            message = "Can't load script file:" + url.toExternalForm();
        }
    }
