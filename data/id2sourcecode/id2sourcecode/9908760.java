    protected void handleClassLoader(Request request, Response response, ClassLoader classLoader) {
        MetadataService metadataService = getMetadataService();
        if (request.getMethod().equals(Method.GET) || request.getMethod().equals(Method.HEAD)) {
            String path = request.getResourceRef().getPath();
            URL url = null;
            Date modificationDate = null;
            if ((path != null) && path.startsWith("/")) {
                path = path.substring(1);
            }
            if (classLoader != null) {
                url = classLoader.getResource(Reference.decode(path));
            } else {
                getLogger().warning("Unable to get the resource. The selected classloader is null.");
            }
            if (url != null) {
                if (url.getProtocol().equals("file")) {
                    File file = new File(url.getFile());
                    modificationDate = new Date(file.lastModified());
                    if (file.isDirectory()) {
                        url = null;
                    }
                }
            }
            if (url != null) {
                try {
                    Representation output = new InputRepresentation(url.openStream(), metadataService.getDefaultMediaType());
                    output.setLocationRef(request.getResourceRef());
                    output.setModificationDate(modificationDate);
                    long timeToLive = getTimeToLive();
                    if (timeToLive == 0) {
                        output.setExpirationDate(null);
                    } else if (timeToLive > 0) {
                        output.setExpirationDate(new Date(System.currentTimeMillis() + (1000L * timeToLive)));
                    }
                    String name = path.substring(path.lastIndexOf('/') + 1);
                    Entity.updateMetadata(name, output, true, getMetadataService());
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
