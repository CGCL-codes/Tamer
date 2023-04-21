    public HTTPResponse generateCapabilities(Map<String, String> Parameters) throws Exception {
        InputStream fr = null;
        fr = new FileInputStream("capabilities.xml");
        if (fr == null) {
            log.debug("Couldn't find capabilities resource in filesystem. Attempting to load from file from package.");
            fr = ClassLoader.getSystemResourceAsStream("capabilities.xml");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (fr.available() > 0) {
            baos.write(fr.read());
        }
        HTTPResponse response = new HTTPResponse();
        response.setMimeType("application/vnd.ogc.wms_xml");
        response.setContent(baos.toByteArray());
        fr.close();
        baos.close();
        return response;
    }
