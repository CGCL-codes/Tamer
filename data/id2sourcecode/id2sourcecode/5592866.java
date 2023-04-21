    private void loadRemoteResource(URL url) throws IOException, JclException {
        logger.debug("Attempting to load a remote resource.");
        if (url.toString().toLowerCase().endsWith(".jar")) {
            loadJar(url);
            return;
        }
        InputStream stream = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int byt;
        while (((byt = stream.read()) != -1)) {
            out.write(byt);
        }
        byte[] content = out.toByteArray();
        if (jarEntryContents.containsKey(url.toString())) {
            if (!Configuration.supressCollisionException()) throw new JclException("Resource " + url.toString() + " already loaded"); else {
                logger.debug("Resource " + url.toString() + " already loaded; ignoring entry...");
                return;
            }
        }
        logger.debug("Loading remote resource.");
        jarEntryContents.put(url.toString(), content);
        out.close();
        stream.close();
    }
