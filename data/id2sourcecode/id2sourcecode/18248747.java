    public void loadResource(String resource) throws FCException {
        log.trace("enter loadResource(String resource)");
        try {
            String url;
            try {
                url = new URL(resource).toString();
            } catch (MalformedURLException mue) {
                log.debug("Failed to create url from resource: " + resource, mue);
                try {
                    url = new File(resource).toURL().toString();
                } catch (Exception e) {
                    log.debug("Failed to create file from resource: " + resource, e);
                    url = resource;
                }
            }
            InputStream stream = null;
            if (url.startsWith("http")) {
                FCService http = getRuntime().getServiceFor(FCService.HTTP_DOWNLOAD);
                stream = http.perform(new FCValue[] { new FCValue(resource) }).getAsInputStream();
            } else if (url.startsWith("file")) {
                stream = new URL(url).openStream();
            } else {
                throw new FCException("Unsupported protocol.");
            }
            _document = _builder.build(stream);
        } catch (Exception e) {
            throw new FCException(e.getMessage(), e);
        }
    }
