    protected void loadProperties(ClassLoader al, URL url) {
        InputStream is = null;
        try {
            is = url.openStream();
            if (is == null) {
                log("Could not load definitions from " + url, Project.MSG_WARN);
                return;
            }
            Properties props = new Properties();
            props.load(is);
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) {
                name = ((String) keys.nextElement());
                classname = props.getProperty(name);
                addDefinition(al, name, classname);
            }
        } catch (IOException ex) {
            throw new BuildException(ex, getLocation());
        } finally {
            FileUtils.close(is);
        }
    }
