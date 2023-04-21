    public static InputStream xpoststreamUrl2(String url, AttrAccess httpArgs, String content) {
        try {
            InputStream in = null;
            URLConnection uc = (new URL(url)).openConnection();
            uc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(uc.getOutputStream());
            wr.write(content);
            wr.flush();
            logger.trace(3, "hdr: ", uc.getHeaderFields());
            String resCode = uc.getHeaderField(0);
            in = uc.getInputStream();
            if (!resCode.endsWith("200 OK")) {
                String msg = "Could not fetch data from url " + url + ", resCode=" + resCode;
                logger.trace(3, msg);
                RuntimeException rte = new RuntimeException(msg);
                if (uc.getContentType().endsWith("xml")) {
                    DOMParser parser = new DOMParser();
                    try {
                        parser.parse(new InputSource(in));
                    } catch (SAXException e) {
                        RuntimeException iex = new RuntimeException("Error while processing document at " + url);
                        iex.initCause(e);
                        throw iex;
                    }
                }
                throw rte;
            }
            return in;
        } catch (MalformedURLException e) {
            RuntimeException iex = new IllegalArgumentException();
            iex.initCause(e);
            throw iex;
        } catch (IOException e1) {
            throw new RuntimeException("Connection exception for url=" + url, e1);
        }
    }
