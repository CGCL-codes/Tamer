    protected String getDocumentByURI(String uri, String declCharset, int maxLength) throws ExecException {
        try {
            URL url = URI.create(uri).toURL();
            URLConnection urlconn = url.openConnection();
            urlconn.setAllowUserInteraction(false);
            urlconn.setDefaultUseCaches(true);
            urlconn.setDoInput(true);
            urlconn.setDoOutput(false);
            urlconn.connect();
            int csize = urlconn.getContentLength();
            if (csize < 0) {
                csize = maxLength;
            } else {
                if (csize > maxLength) {
                    throw new ExecException("Input XQuery file too big");
                }
            }
            byte[] buf = new byte[csize];
            InputStream is = (InputStream) urlconn.getContent();
            int i = 0;
            int ret = 0;
            while (i < csize) {
                ret = is.read(buf, i, csize - i);
                if (ret < 0) {
                    break;
                }
                i += ret;
            }
            is.close();
            String val;
            if (declCharset != null) {
                val = new String(buf, 0, i, declCharset);
            } else {
                val = new String(buf, 0, i);
            }
            return val;
        } catch (Exception e) {
            throw new ExecException("Failed to download document content by URI: " + e);
        }
    }
