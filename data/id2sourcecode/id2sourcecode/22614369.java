    private void checkDependencies() {
        if (log.isDebugEnabled()) {
            log.debug("Reading: " + url + " (prepare)...");
        }
        SAXReader reader = new SAXReader();
        InputStream is = null;
        try {
            is = url.openStream();
            Document document = reader.read(is);
            Element root = document.getRootElement();
            id = Dom4jUtil.getChildTextTrimChecked(root, "module-id");
            String depConfig = Dom4jUtil.getChildTextTrimOptional(root, "depends-on", null);
            this.deps = new HashSet<String>();
            if (depConfig != null) {
                String[] depsArray = StringUtils.split(depConfig, ",");
                for (int i = 0; i < depsArray.length; i++) {
                    deps.add(depsArray[i]);
                }
            }
        } catch (Exception e) {
            log.error("Error reading menu config (prepare) from " + url, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                log.error("Error closing input stream (prepare).", e);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Menu config prepared: " + url);
        }
    }
