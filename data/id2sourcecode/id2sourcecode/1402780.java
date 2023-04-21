    public static Element postMessage(final String data) throws Exception {
        final URL theUrl = getHostURL();
        final HttpURLConnection urlConn = (HttpURLConnection) (theUrl).openConnection();
        urlConn.setRequestMethod("POST");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        final BufferedOutputStream bos = new BufferedOutputStream(urlConn.getOutputStream());
        final String request = data;
        bos.write(request.getBytes(), 0, request.length());
        try {
            bos.close();
        } catch (final Exception e) {
            logger.finest("Exception closing bos : " + e);
        }
        final InputStream bis = urlConn.getInputStream();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer)) > -1) {
            baos.write(buffer, 0, count);
        }
        final SAXBuilder sb = new SAXBuilder();
        logger.finest("Received XML response from server: " + baos.toString());
        return sb.build(new StringReader(baos.toString())).getRootElement();
    }
