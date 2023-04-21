    public Templates getTemplates(SAXTransformerFactory factory, String path) {
        try {
            Templates templates = getTemplatesFromCache(path);
            if (templates == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Creating new Templates for " + path);
                }
                long trace = 0;
                if (log.isDebugEnabled()) {
                    trace = System.currentTimeMillis();
                }
                Source source = null;
                long lastModified = 0;
                URL url = null;
                try {
                    if (resolver != null) {
                        url = resolver.resolve(path);
                    } else {
                        url = new URL(path);
                    }
                } catch (MalformedURLException ex) {
                    url = new File(path).toURL();
                }
                if ("file".equalsIgnoreCase(url.getProtocol()) && autoReloadTemplates) {
                    if (log.isDebugEnabled()) {
                        log.debug("Loading template from filesystem if changed");
                    }
                    File file = new File(url.getPath());
                    if (!file.exists()) {
                        log.error("Stylesheet " + path + " cannot be found");
                        return null;
                    }
                    source = new StreamSource(new FileInputStream(file));
                    source.setSystemId(file.getAbsolutePath());
                    lastModified = file.lastModified();
                } else {
                    source = new StreamSource(url.openStream());
                    source.setSystemId(url.toString());
                    if (log.isDebugEnabled()) {
                        log.debug("Loading template from url");
                    }
                }
                templates = factory.newTemplates(source);
                if (log.isDebugEnabled()) {
                    log.debug("Template compilation time:" + (System.currentTimeMillis() - trace));
                }
                putTemplates(templates, path, lastModified);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Reusing Templates for " + path);
                }
            }
            return templates;
        } catch (Exception e) {
            log.error("Exception in creating Transform Handler", e);
            return null;
        }
    }
