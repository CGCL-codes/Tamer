    protected void handleClassLoader(Request request, Response response, ClassLoader classLoader) {
        MetadataService metadataService = getMetadataService(request);
        if (request.getMethod().equals(Method.GET) || request.getMethod().equals(Method.HEAD)) {
            String path = request.getResourceRef().getPath();
            if ((path != null) && path.startsWith("/")) path = path.substring(1);
            URL url = classLoader.getResource(Reference.decode(path));
            if (url != null) {
                if (url.getProtocol().equals("file")) {
                    File file = new File(url.getFile());
                    if (file.isDirectory()) {
                        url = null;
                    }
                }
            }
            if (url != null) {
                try {
                    Representation output = new InputRepresentation(url.openStream(), metadataService.getDefaultMediaType());
                    output.setIdentifier(request.getResourceRef());
                    String name = path.substring(path.lastIndexOf('/') + 1);
                    updateMetadata(metadataService, name, output);
                    response.setEntity(output);
                    response.setStatus(Status.SUCCESS_OK);
                } catch (IOException ioe) {
                    getLogger().log(Level.WARNING, "Unable to open the representation's input stream", ioe);
                    response.setStatus(Status.SERVER_ERROR_INTERNAL);
                }
            } else {
                response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            }
        } else {
            response.setStatus(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
            response.getAllowedMethods().add(Method.GET);
            response.getAllowedMethods().add(Method.HEAD);
        }
    }
