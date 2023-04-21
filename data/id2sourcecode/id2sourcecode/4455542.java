    private <T extends Response> T request(TreeMap<String, String> parameters, BasicHandler<T> handler) {
        signParams(METHOD, HOST, PATH, awsSecretAccessKey, parameters);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            SAXParser parser = factory.newSAXParser();
            URL url = new URL(PROTOCOL + "://" + HOST + PATH);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + ENCODING);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            os.write(query(parameters).getBytes(ENCODING));
            os.close();
            parser.parse(connection.getInputStream(), handler);
            return handler.response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
