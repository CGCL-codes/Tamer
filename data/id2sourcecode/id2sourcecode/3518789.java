    public static InputStream streamUrl(String url, AttrAccess... httpArgs) {
        try {
            InputStream in = null;
            if (url.startsWith("file:")) {
                in = (new URL(url)).openStream();
            } else if (url.startsWith("http:")) {
                URLConnection uc = (new URL(url)).openConnection();
                if (httpArgs.length > 0 && httpArgs[0] != null) {
                    for (String a : httpArgs[0].getPopulatedAttr()) uc.setRequestProperty(a, httpArgs[0].sa(a));
                }
                logger.trace(3, "hdr: ", uc.getHeaderFields());
                String resCode = uc.getHeaderField(0);
                in = uc.getInputStream();
                if (!resCode.endsWith("200 OK")) {
                    String msg = "Could not fetch data from url " + url + ", resCode=" + resCode;
                    logger.trace(3, msg);
                    CommRtException rte = new CommRtException(msg);
                    if (uc.getContentType().endsWith("xml")) {
                        DOMParser parser = new DOMParser();
                        try {
                            parser.parse(new InputSource(in));
                        } catch (SAXException e) {
                            RuntimeException iex = new CommRtException("Error while processing document at " + url);
                            iex.initCause(e);
                            throw iex;
                        }
                    }
                    throw rte;
                }
            } else {
                File file = new File(url);
                in = file.toURL().openStream();
            }
            return in;
        } catch (MalformedURLException e) {
            RuntimeException iex = new IllegalArgumentException();
            iex.initCause(e);
            throw iex;
        } catch (IOException e1) {
            CommRtException rte = new CommRtException("Connection exception for url=" + url);
            rte.initCause(e1);
            throw rte;
        }
    }
