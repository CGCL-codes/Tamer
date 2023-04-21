    private InputStream prepareInputStream(String urlToRetrieve) throws IOException {
        URL url = new URL(urlToRetrieve);
        URLConnection uc = url.openConnection();
        if (timeOut > 0) {
            uc.setConnectTimeout(timeOut);
            uc.setReadTimeout(timeOut);
        }
        prepareUC(uc, urlToRetrieve);
        InputStream is = uc.getInputStream();
        if ("gzip".equals(uc.getContentEncoding())) is = new GZIPInputStream(is);
        return is;
    }
