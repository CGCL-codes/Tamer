    private static String getWSDL(final URL url) throws ClientToolkitException {
        try {
            InputStreamReader input = new InputStreamReader(url.openStream());
            StringBuffer buf = new StringBuffer();
            char[] charArray = new char[1024];
            int readBytes;
            while ((readBytes = input.read(charArray)) >= 0) {
                buf.append(charArray, 0, readBytes);
            }
            String wsdl = buf.toString();
            return wsdl;
        } catch (IOException e) {
            ServerURLInvalidException exception = new ServerURLInvalidException(url, e);
            throw exception;
        }
    }
