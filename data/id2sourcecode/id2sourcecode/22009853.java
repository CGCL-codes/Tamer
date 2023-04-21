    private void serveResource(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String resourceName = ClientValidatorUtils.getResourceName(requestMap);
        String resourceType = ClientValidatorUtils.getResourceType(requestMap);
        String contentType = ClientValidatorUtils.getContentType(resourceType);
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try {
            URL url = ValidatorResourceLoader.class.getResource("/META-INF/" + resourceName + "." + resourceType);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response.setContentType(contentType);
            response.setStatus(200);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), response.getCharacterEncoding());
            String line = reader.readLine();
            while (line != null) {
                outputStreamWriter.write(line + "\n");
                line = reader.readLine();
            }
            outputStreamWriter.flush();
            outputStreamWriter.close();
            facesContext.responseComplete();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
