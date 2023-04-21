    protected void processXML(HttpServletRequest request, HttpServletResponse response, InputStream msgStream) throws IOException, MarinerContextException, SAXException {
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1028];
        int read = msgStream.read(buffer);
        while (read != -1) {
            os.write(buffer, 0, read);
            read = msgStream.read(buffer);
        }
        os.flush();
    }
