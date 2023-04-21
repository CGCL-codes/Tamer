    public Resource readResource(URL url, ResourceManager resourceManager) throws NAFException {
        Resource resource = new Resource(resourceManager, url);
        InputStream in = null;
        try {
            in = url.openStream();
            Reader rd = new InputStreamReader(in, "UTF-8");
            threadResource.set(resource);
            Context cx = Context.enter();
            try {
                cx.evaluateReader(scope, rd, url.toExternalForm(), 1, null);
            } finally {
                Context.exit();
                threadResource.set(null);
            }
            return resource;
        } catch (Exception ex) {
            throw new NAFException("Error reading JavaScript resource \"" + url.toExternalForm() + "\"", ex);
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception ignored) {
            }
        }
    }
