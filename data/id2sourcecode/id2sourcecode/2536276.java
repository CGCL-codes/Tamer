    public void dumpIndex(Writer out) throws Exception {
        HttpRequest httpReq = new HttpRequest(new URL(remoteIndexUrl));
        CGIParameters params = new CGIParameters();
        params.put("cmd", Constants.CGI_PARAM_CMD_DUMP_INDEX);
        InputStream is = null;
        try {
            is = httpReq.get(params);
            BufferedInputStream bis = new BufferedInputStream(is);
            int read = 0;
            while ((read = bis.read()) != -1) {
                out.write((char) read);
            }
        } finally {
            if (is != null) is.close();
        }
    }
