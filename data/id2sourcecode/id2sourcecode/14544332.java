    public void reload() {
        wasLoaded = false;
        InputStream is = null;
        try {
            if (url.getProtocol().equals("file")) {
                File f = new File(url.toURI());
                long timeStamp = f.lastModified();
                if (timeStamp == fileTimeStamp) return;
                fileTimeStamp = timeStamp;
            }
            is = url.openStream();
            GroovyObject oldObject = groovyObject;
            groovyObject = (GroovyObject) engine.loadClass(is);
            groovyObject.setProperty("page", this);
            if (eventHandler == null) eventHandler = new GroovyEventHandler(project, this, new ValidationHandler(this));
            if (oldObject != null) {
                List properties = oldObject.getMetaClass().getProperties();
                for (Object property : properties) {
                    MetaProperty metaProperty = (MetaProperty) property;
                    try {
                        String key = metaProperty.getName();
                        if (!key.equals("class") && !key.equals("metaClass")) groovyObject.setProperty(key, oldObject.getProperty(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            wasLoaded = true;
        } catch (Exception ioe) {
            DebugLogger.logError("BUILDER", "Unable to load the Groovy object: " + url.getFile());
            ioe.printStackTrace();
        } finally {
            closeStream(is);
        }
    }
