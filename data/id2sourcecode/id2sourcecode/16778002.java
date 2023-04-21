    private static Element sendRequest(String strUrl, String params) throws TechnoratiApiException {
        URLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;
        Element root = null;
        try {
            final URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            logger.debug("requesting URL: {}?{}", url, params);
            out = conn.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            in = conn.getInputStream();
            final SAXBuilder builder = new SAXBuilder();
            builder.setEntityResolver(new DTDEntityResolver());
            final Document doc = builder.build(in);
            root = doc.getRootElement().getChild("document");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new TechnoratiApiException(e);
        } finally {
            closeQuietly(out);
            closeQuietly(in);
        }
        handleErrors(root);
        return root;
    }
